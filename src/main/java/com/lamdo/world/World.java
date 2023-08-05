package com.lamdo.world;

import com.lamdo.block.Block;
import com.lamdo.block.Blocks;
import com.lamdo.block.util.Blockstate;
import com.lamdo.entity.player.Player;
import com.lamdo.render.model.Mesh;
import com.lamdo.util.MathUtil;
import com.lamdo.util.Noise;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class World {

    private final int RENDER_DISTANCE = 8;

    // range of chunks that should be kept in memory
    // when a save system is implemented, chunks outside this range will be saved to the disk unless they were never touched
    private final int IN_MEMORY_DISTANCE = 25;

    private Vector3d playerPos;

    private Map<Vector2i, Chunk> chunks;

    public World() {
        chunks = new HashMap<Vector2i, Chunk>();
    }

    public void updatePlayerPos(Vector3d playerPos) {
        this.playerPos = playerPos;
    }

    public void update() {
        generateChunksInRenderDistance();
        generateTerrains();
        generateMeshes();
        applyMeshes();
    }

    private void generateChunksInRenderDistance() {
        for(Chunk chunk: chunks.values()) {
            chunk.setActive(false);
        }

        Vector2i playerChunkCoord = getChunkCoord((int)playerPos.x, (int)playerPos.z);

        // Generate chunks in the render distance
        for(int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; x++) {
            for(int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; z++) {

                Vector2i chunkCoord = new Vector2i(x, z).add(playerChunkCoord);
                if(chunks.containsKey(chunkCoord)) {
                    Chunk chunk = chunks.get(chunkCoord);
                    chunk.setActive(true);
                } else {
                    Chunk newChunk = new Chunk(chunkCoord.x, chunkCoord.y, this);
                    chunks.put(chunkCoord, newChunk);
                    updateSurroundingChunks(chunkCoord);
                }
            }
        }

        // Check if the chunk should be unloaded from memory
        Vector2i[] chunkCoords = chunks.keySet().toArray(new Vector2i[chunks.size()]);
        for(int i = 0; i < chunkCoords.length; i++) {
            Vector2i coord = chunkCoords[i];
            if(!checkIfChunkInLoadedDistance(coord, playerChunkCoord)) {
                chunks.remove(coord);
            }
        }
    }

    private boolean checkIfChunkInLoadedDistance(Vector2i coord, Vector2i originCoord) {
        return coord.x > originCoord.x - IN_MEMORY_DISTANCE && coord.y > originCoord.y - IN_MEMORY_DISTANCE && coord.x < originCoord.x + IN_MEMORY_DISTANCE && coord.y < originCoord.y + IN_MEMORY_DISTANCE;
    }

    public Blockstate generateTerrain(int x, int y, int z) {

        Blockstate generatedBlockstate;

        // IMMUTABLE PASS
        if(y == 0) return Blocks.BEDROCK.getDefaultBlockstate();

        // TERRAIN HEIGHT PASS
        int terrainHeight = 30 + (int) Math.floor(Noise.noise2D(0, x, z, 100f, 0, 3, 0.5f, 2) * 20);
        if(y > terrainHeight) {
            generatedBlockstate = Blocks.AIR.getDefaultBlockstate();
        } else if (y == terrainHeight) {
            generatedBlockstate = Blocks.GRASS.getDefaultBlockstate();
        } else if (y >= terrainHeight - 3) {
            generatedBlockstate = Blocks.DIRT.getDefaultBlockstate();
        } else {
            generatedBlockstate = Blocks.STONE.getDefaultBlockstate();
        }

        return generatedBlockstate;

    }

    private void updateSurroundingChunks(Vector2i coord) {
        for(int xoff = -1; xoff <= 1; xoff += 2) {
            for(int zoff = -1; zoff <= 1; zoff += 2) {
                Vector2i offsetCoord = new Vector2i(coord).add(xoff, zoff);
                if(chunks.containsKey(offsetCoord)) {
                    chunks.get(offsetCoord).markDirty();
                }
            }
        }
    }

    public void updateBlock(Vector3i blockPos, Block block) {
        updateBlock(blockPos.x, blockPos.y, blockPos.z, block);
    }

    public void markDirty(Vector3i blockPos) {
        int x = blockPos.x;
        int y = blockPos.y;
        int z = blockPos.z;
        if(isInsideWorld(x, y, z)) {
            // find the coordinate of the chunk that contains this block
            Vector2i chunkCoord = getChunkCoord(x, z);

            // ask the chunk for the block
            Chunk chunk = chunks.get(chunkCoord);
            chunk.markDirty();
        }
    }

    public void updateBlock(int x, int y, int z, Block block) {
        if(isInsideWorld(x, y, z)) {
            // find the coordinate of the chunk that contains this block
            Vector2i chunkCoord = getChunkCoord(x, z);

            // ask the chunk for the block
            Chunk chunk = chunks.get(chunkCoord);
            int localX = MathUtil.mod(x, Chunk.WIDTH);
            int localZ = MathUtil.mod(z, Chunk.WIDTH);
            chunk.updateBlockLocal(localX, y, localZ, block);
        }
    }

    public Blockstate getBlockstate(Vector3i blockPos) {
        return getBlockstate(blockPos.x, blockPos.y, blockPos.z);
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

    public static Vector2i getChunkCoord(int worldX, int worldZ) {
        int x = worldX;
        int z = worldZ;
        if(x < 0) x -= 15;
        if(z < 0) z -= 15;
        int chunkX = x / Chunk.WIDTH;
        int chunkZ = z / Chunk.WIDTH;
        return new Vector2i(chunkX, chunkZ);
    }

    private boolean isInsideWorld(int x, int y, int z) {
        if (y < 0 || y >= Chunk.HEIGHT) return false;

        Vector2i coord = getChunkCoord(x, z);
        return chunks.containsKey(coord);
    }

    public void generateTerrains() {
        for(Chunk chunk: chunks.values()) {
            if(!chunk.isTerrainGenerated())
                chunk.generateTerrain();
        }
    }

    public void generateMeshes() {
        for(Chunk chunk: chunks.values()) {
            if(chunk.isActive() && chunk.isDirty())
                chunk.createMesh();
        }
    }

    public void applyMeshes() {
        for(Chunk chunk: chunks.values()) {
            if(chunk.isActive() && chunk.isDirty() && chunk.meshGenerated())
                chunk.applyMesh();
        }
    }

    public Collection<Chunk> getChunks() {
        return chunks.values().stream().filter(Chunk::isActive).toList();
//        return chunks.values();
    }

}
