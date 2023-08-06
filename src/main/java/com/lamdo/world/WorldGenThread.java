package com.lamdo.world;

public class WorldGenThread extends Thread{

    private final World world;
    private boolean stopRequested = false;

    public WorldGenThread(World world) {
        this.world = world;
    }

    public void requestStop() {
        this.stopRequested = true;
    }

    @Override
    public void run() {
        while(!stopRequested) {
            synchronized (world) {
                world.generateChunksInRenderDistance();
                world.generateTerrainBatch();
                world.generateMeshBatch();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}
        }
    }
}
