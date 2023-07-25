package com.lamdo;

import com.lamdo.entity.player.Camera;
import com.lamdo.entity.player.Player;
import com.lamdo.render.Loader;
import com.lamdo.render.Window;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.renderer.MasterRenderer;
import com.lamdo.render.shader.VoxelShader;
import com.lamdo.world.Chunk;
import com.lamdo.world.World;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL33.*;

public class Main {

    public static void main(String[] args) {

        // Create the game window
        Window window = new Window();

        // Set the texture that the voxels will use
        VoxelModel.setTexture(Loader.loadTextureAtlas("/textures/atlas.png"), 4);
        World world = new World();

        MasterRenderer renderer = new MasterRenderer();
        Player player = new Player(new Vector3f(0, 45, 0), world);
        Camera camera = new Camera(player);

        world.generateTerrains();
        world.generateMeshes();

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);

            player.move();

            renderer.render(world, camera);

            window.update();
        }

        window.terminate();

    }
}