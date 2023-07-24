package com.lamdo;

import com.lamdo.entity.player.Camera;
import com.lamdo.render.Loader;
import com.lamdo.render.Window;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.renderer.MasterRenderer;
import com.lamdo.render.shader.VoxelShader;
import com.lamdo.world.Chunk;
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

        // Set the texture that the voxels will use, in the future this will be a texture atlas
        VoxelModel.setTexture(Loader.loadTexture("/textures/dirt.png"), 1);

        MasterRenderer renderer = new MasterRenderer();
        Camera camera = new Camera();

        Chunk chunk = new Chunk(0,0);
        chunk.generateTerrain();
        chunk.generateMesh();

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);

            camera.move();

            renderer.render(chunk.getVoxelModel(), camera);

            window.update();
        }

        window.terminate();

    }
}