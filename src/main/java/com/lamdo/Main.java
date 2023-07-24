package com.lamdo;

import com.lamdo.entity.player.Camera;
import com.lamdo.render.Loader;
import com.lamdo.render.Window;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.renderer.MasterRenderer;
import com.lamdo.render.shader.VoxelShader;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL33.*;

public class Main {

    public static void main(String[] args) {

        Window window = new Window();

        float[] positions = {
                -0.5f,  0.5f, -1.0f,
                -0.5f, -0.5f, -1.0f,
                 0.5f, -0.5f, -1.0f,
                 0.5f,  0.5f, -1.0f
        };

        float[] textureCoords = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        RawModel model = Loader.loadToVAO(positions, textureCoords, indices);
        VoxelModel voxelModel = new VoxelModel(model, new Vector3f(0, 0, 0));
        VoxelModel.setTexture(Loader.loadTexture("/textures/dirt.png"), 1);
        MasterRenderer renderer = new MasterRenderer();

        Camera camera = new Camera();

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);

            camera.move();

            renderer.render(voxelModel, camera);

            window.update();
        }

        window.terminate();

    }
}