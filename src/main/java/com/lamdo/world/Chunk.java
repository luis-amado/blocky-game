package com.lamdo.world;

import com.lamdo.block.Block;
import com.lamdo.block.Blocks;
import com.lamdo.block.util.Blockstate;
import com.lamdo.render.Loader;
import com.lamdo.render.model.Mesh;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.util.ArrayUtils;
import com.lamdo.util.Direction;
import com.lamdo.util.Noise;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chunk {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 128;

    private World world;
    private Vector2i coord;
    private VoxelModel voxelModel;
    private short[] blockstates;
    private boolean active;
    private boolean dirty;
    private Mesh mesh;
    private boolean meshCreated;
    private boolean meshApplied;
    private boolean terrainGenerated;

    public Chunk(int x, int z, World world) {
        this.world = world;
        this.coord = new Vector2i(x, z);
        voxelModel = new VoxelModel(new Vector3f(x * WIDTH, 0, z * WIDTH));
        blockstates = new short[WIDTH * HEIGHT * WIDTH];
        active = true;
        dirty = true;
        terrainGenerated = false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean meshGenerated() {
        return meshCreated;
    }

    public boolean isTerrainGenerated() {
        return this.terrainGenerated;
    }

    public boolean meshApplied() {
        return this.meshApplied;
    }

    public void generateTerrain() {
        for(int x = 0; x < WIDTH; x++) {
            for(int z = 0; z < WIDTH; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    Vector3i worldCoords = toWorldCoord(x, y, z);
                    blockstates[index(x, y, z)] = world.generateTerrain(worldCoords.x, worldCoords.y, worldCoords.z).getID();
                }
            }
        }
        terrainGenerated = true;
    }

    public void markDirty() {
        this.dirty = true;
        this.meshCreated = false;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void updateBlockLocal(int x, int y, int z, Block block) {
        blockstates[index(x, y, z)] = block.getDefaultBlockstate().getID();
        markDirty();
        for(Direction face: Direction.horizontalDirections()) {
            Vector3i pos = new Vector3i(x,y,z);
            pos.add(face.getNormal());
            if(!isInThisChunk(pos.x, pos.z)) {
                Vector3i worldPos = toWorldCoord(pos.x, pos.y, pos.z);
                world.markDirty(worldPos);
            }
        }
    }

    public void createMesh() {
        if(!terrainGenerated) {
            System.out.println("Tried to generate the mesh of a chunk that doesn't have a generated terrain");
            System.exit(-1);
        }
        mesh = generateMesh();
        meshCreated = true;
        meshApplied = false;
    }

    public void applyMesh() {
        if(voxelModel.hasModel()) {
            Loader.updateVAO(voxelModel.getModel(), mesh.positions(), mesh.textureCoords(), mesh.indices());
        } else {
            voxelModel.setModel(Loader.loadToVAO(mesh.positions(), mesh.textureCoords(), mesh.indices()));
        }
        dirty = false;
        meshApplied = true;
    }

    private Mesh generateMesh() {
        List<Float> positions = new ArrayList<Float>();
        List<Float> textureCoords = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();

        int vertexCount = 0;

        for(int x = 0; x < WIDTH; x++) {
            for(int z = 0; z < WIDTH; z++) {
                for (int y = 0; y < HEIGHT; y++) {

                    Blockstate blockstate = getBlockstateLocal(x, y, z);
                    Block block = blockstate.getBlock();
                    if(!block.isSolid()) continue; // skip air blocks
                    for(Direction face: Direction.values()) {

                        // check if there is a block obstructing this face
                        Vector3i faceNormal = face.getNormal();
                        Blockstate faceBlockstate = getBlockstateLocal(x + faceNormal.x, y + faceNormal.y, z + faceNormal.z);
                        Block faceBlock = faceBlockstate.getBlock();
                        if(faceBlock.isSolid() && !faceBlock.isTransparent()) continue;

                        float[] facePositions = face.getFaceVoxelVertices(x, y, z);
                        float[] faceCoords = VoxelModel.getTextureCoords(blockstate.getTextures().getFaceTexture(face));
                        int[] faceIndices = new int[]{vertexCount, vertexCount+1, vertexCount+2, vertexCount+2, vertexCount+3, vertexCount};
                        vertexCount += 4;

                        ArrayUtils.addFloatArrayToList(facePositions, positions);
                        ArrayUtils.addFloatArrayToList(faceCoords, textureCoords);
                        ArrayUtils.addIntArrayToList(faceIndices, indices);
                    }

                }
            }
        }
        return new Mesh(positions, textureCoords, indices);
    }

    private Vector3i toWorldCoord(int x, int y, int z) {
        return new Vector3i(x + coord.x * WIDTH, y, z + coord.y * WIDTH);
    }

    public Blockstate getBlockstateLocal(int x, int y, int z) {
        if(isInsideChunkLocal(x, y, z)) {
            return Blockstate.fromID(blockstates[index(x, y, z)]);
        } else {
            // We need to know a block that is outside this chunk so we ask the world.
            Vector3i worldCoord = toWorldCoord(x, y, z);
            return world.getBlockstate(worldCoord.x, worldCoord.y, worldCoord.z);
        }
    }

    private boolean isInsideChunkLocal(int x, int y, int z) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && z >= 0 && z < WIDTH;
    }

    private boolean isInThisChunk(int x, int z) {
        return x >= 0 && x < WIDTH && z >= 0 && z < WIDTH;
    }

    private int index(int x, int y, int z) {
        return x + z * WIDTH + y * WIDTH * WIDTH;
    }

    public VoxelModel getVoxelModel() {
        return voxelModel;
    }

}
