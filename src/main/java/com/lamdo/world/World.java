package com.lamdo.world;

import com.lamdo.block.Blocks;
import com.lamdo.block.util.Blockstate;
import com.lamdo.util.MathUtil;
import org.joml.Vector2i;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class World {

    private final int RENDER_DISTANCE = 2;

    private Map<Vector2i, Chunk> chunks;

    public World() {
        chunks = new HashMap<Vector2i, Chunk>();

        // Generate chunks in the render distance
        for(int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; x++) {
            for(int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; z++) {
                Chunk newChunk = new Chunk(x, z, this);
                Vector2i chunkCoord = new Vector2i(x, z);
                chunks.put(chunkCoord, newChunk);
            }
        }
    }

    public Blockstate getBlockstate(int x, int y, int z) {
        if(isInsideWorld(x, y, z)) {
            // find the coordinate of the chunk that contains this block
            Vector2i chunkCoord = getChunkCoord(x, z);

            // ask the chunk for the block
            Chunk chunk = chunks.get(chunkCoord);
            int localX = MathUtil.mod(x, Chunk.WIDTH);
            int localZ = MathUtil.mod(z, Chunk.WIDTH);
            return chunk.getBlockstateLocal(localX, y, localZ);
        } else {
            return Blocks.AIR.getDefaultBlockstate();
        }
    }

    private Vector2i getChunkCoord(int worldX, int worldZ) {
        int x = worldX;
        int z = worldZ;
        if(x < 0) x -= 15;
        if(z < 0) z -= 15;
        int chunkX = x / Chunk.WIDTH;
        int chunkZ = z / Chunk.WIDTH;
        return new Vector2i(chunkX, chunkZ);
    }

    private boolean isInsideWorld(int x, int y, int z) {
        int worldWidth = RENDER_DISTANCE * Chunk.WIDTH;
        return x >= -worldWidth && x < worldWidth + 16 && y >= 0 && y < Chunk.HEIGHT && z >= -worldWidth && z < worldWidth + 16;
    }

    public void generateTerrains() {
        for(Chunk chunk: chunks.values()) {
            chunk.generateTerrain();
        }
    }

    public void generateMeshes() {
        for(Chunk chunk: chunks.values()) {
            chunk.generateMesh();
        }
    }

    public Collection<Chunk> getChunks() {
        return chunks.values();
    }

}
