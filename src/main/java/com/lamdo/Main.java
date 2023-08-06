package com.lamdo;

import com.lamdo.entity.player.Camera;
import com.lamdo.entity.player.Player;
import com.lamdo.gui.components.Crosshair;
import com.lamdo.gui.components.Hotbar;
import com.lamdo.render.Loader;
import com.lamdo.render.Window;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.renderer.MasterRenderer;
import com.lamdo.world.World;
import com.lamdo.world.WorldGenThread;
import org.joml.Vector3d;

import static org.lwjgl.opengl.GL33.*;

public class Main {

    public static void main(String[] args) {

        // Create the game window
        Window window = new Window();

        // Set the texture that the voxels will use
        VoxelModel.setTexture(Loader.loadTextureAtlas("/textures/new_atlas.png"), 8);

        World world = new World();
        Hotbar hotbar = new Hotbar();

        MasterRenderer renderer = new MasterRenderer();
        Player player = new Player(new Vector3d(0, 45, 0), world, hotbar);
        Camera camera = new Camera(player);
        world.updatePlayerPos(player.getPosition());
        world.init();

        Crosshair crosshair = new Crosshair();

        WorldGenThread worldGenThread = new WorldGenThread(world);
        worldGenThread.start();

        while(!window.shouldClose()) {
            if(!player.spectatorModeActive()) {
                hotbar.update();
                hotbar.render();

                if (!Window.debugMode) crosshair.render();
            }

            player.move();

            synchronized (world) {
                world.update();
                renderer.render(world, camera);
            }

            window.update();
        }

        worldGenThread.requestStop();

        renderer.cleanUp();
        Loader.cleanUp();
        window.terminate();

    }
}