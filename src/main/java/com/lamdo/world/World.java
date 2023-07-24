package com.lamdo.world;

import org.joml.Vector2i;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class World {

    private final int RENDER_DISTANCE = 1;

    private Map<Vector2i, Chunk> chunks;

    public World() {
        chunks = new HashMap<Vector2i, Chunk>();

        // Generate chunks in the render distance
        for(int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; x++) {
            for(int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; z++) {
                Chunk newChunk = new Chunk(x, z);
                Vector2i chunkCoord = new Vector2i(x, z);
                chunks.put(chunkCoord, newChunk);
            }
        }
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
