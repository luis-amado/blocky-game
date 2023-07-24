package com.lamdo.world;

import com.lamdo.render.Loader;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.util.ArrayUtils;
import com.lamdo.util.Direction;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    public static final int WIDTH = 1;
    public static final int HEIGHT = 2;

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
                for (int y = 0; y < HEIGHT; y++) {
                    // Fill the entire 'chunk'
                    blockstates[index(x, y, z)] = 1;
                }
            }
        }
    }

    public void generateMesh() {
        List<Float> positions = new ArrayList<Float>();
        List<Float> textureCoords = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();

        int vertexCount = 0;

        for(int x = 0; x < WIDTH; x++) {
            for(int z = 0; z < WIDTH; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    short blockstate = blockstates[index(x, y, z)];
                    if(blockstate == 0) continue; // skip air blocks
                    for(Direction face: Direction.values()) {
                        float[] facePositions = face.getFaceVoxelVertices(x, y, z);
                        float[] faceCoords = new float[]{0, 0, 0, 1, 1, 1, 1, 0};
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

    private int index(int x, int y, int z) {
        return x + z * WIDTH + y * WIDTH * WIDTH;
    }

    public VoxelModel getVoxelModel() {
        return voxelModel;
    }

}
