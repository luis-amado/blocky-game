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
import org.joml.Vector3d;

import static org.lwjgl.opengl.GL33.*;

public class Main {

    public static void main(String[] args) {

        // Create the game window
        Window window = new Window();

        // Set the texture that the voxels will use
        VoxelModel.setTexture(Loader.loadTextureAtlas("/textures/atlas.png"), 4);
        World world = new World();

        MasterRenderer renderer = new MasterRenderer();
        Player player = new Player(new Vector3d(0, 45, 0), world);
        Camera camera = new Camera(player);

        world.generateTerrains();
        world.generateMeshes();

        Hotbar hotbar = new Hotbar();
        Crosshair crosshair = new Crosshair();

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);

            player.move();

            hotbar.render();
            if(!Window.debugMode) crosshair.render();
            renderer.render(world, camera);

            window.update();
        }

        window.terminate();

    }
}