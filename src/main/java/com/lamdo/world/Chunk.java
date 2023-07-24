package com.lamdo.world;

import com.lamdo.block.Block;
import com.lamdo.block.Blocks;
import com.lamdo.block.util.Blockstate;
import com.lamdo.render.Loader;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.util.ArrayUtils;
import com.lamdo.util.Direction;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 128;

    private Vector2i coord;
    private VoxelModel voxelModel;
    private short[] blockstates;

    public Chunk(int x, int z) {
        this.coord = new Vector2i(x, z);
        voxelModel = new VoxelModel(new Vector3f(x * WIDTH, 0, z * WIDTH));
        blockstates = new short[WIDTH * HEIGHT * WIDTH];
    }

    public void generateTerrain() {
        for(int x = 0; x < WIDTH; x++) {
            for(int z = 0; z < WIDTH; z++) {
                int terrainHeight = 40;
                for (int y = 0; y < HEIGHT; y++) {

//                    setBlockLocal(x, y, z, Blocks.DIRT);

                    if(y > terrainHeight) {
                        setBlockLocal(x, y, z, Blocks.AIR);
                    } else if (y == terrainHeight) {
                        setBlockLocal(x, y, z, Blocks.GRASS);
                    } else if (y >= terrainHeight - 3) {
                        setBlockLocal(x, y, z, Blocks.DIRT);
                    } else if (y > 0) {
                        setBlockLocal(x, y, z, Blocks.STONE);
                    } else {
                        setBlockLocal(x, y, z, Blocks.BEDROCK);
                    }

                }
            }
        }
    }

    private void setBlockLocal(int x, int y, int z, Block block) {
        blockstates[index(x, y, z)] = block.getDefaultBlockstate().getID();
    }

    public void generateMesh() {
        List<Float> positions = new ArrayList<Float>();
        List<Float> textureCoords = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();

        int vertexCount = 0;

        for(int x = 0; x < WIDTH; x++) {
            for(int z = 0; z < WIDTH; z++) {
                for (int y = 0; y < HEIGHT; y++) {

                    short blockstateID = getBlockstateLocal(x, y, z);
                    Blockstate blockstate = Blockstate.fromID(blockstateID);
                    Block block = blockstate.getBlock();
                    if(!block.isSolid()) continue; // skip air blocks
                    for(Direction face: Direction.values()) {

                        // check if there is a block obstructing this face
                        Vector3i faceNormal = face.getNormal();
                        short faceBlockstate = getBlockstateLocal(x + faceNormal.x, y + faceNormal.y, z + faceNormal.z);
                        Block faceBlock = Blockstate.fromID(faceBlockstate).getBlock();
                        if(faceBlock.isSolid()) continue;

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
        voxelModel.setModel(Loader.loadToVAO(positions, textureCoords, indices));
    }

    private short getBlockstateLocal(int x, int y, int z) {
        if(isInsideChunkLocal(x, y, z)) {
            return blockstates[index(x, y, z)];
        } else {
            return 0; // act like air if it is outside the chunk
        }
    }

    private boolean isInsideChunkLocal(int x, int y, int z) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && z >= 0 && z < WIDTH;
    }

    private int index(int x, int y, int z) {
        return x + z * WIDTH + y * WIDTH * WIDTH;
    }

    public VoxelModel getVoxelModel() {
        return voxelModel;
    }

}
