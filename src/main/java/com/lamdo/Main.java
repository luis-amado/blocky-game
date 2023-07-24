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

        float[] positions = new float[]{
                //north, south, east, west, up, down
                1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0,
                0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1,
                1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0,
                0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1,
                0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
                1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0
        };

        float[] textureCoords = new float[]{
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0
        };

        int[] indices = new int[]{
                0, 1, 2, 2, 3, 0,
                4, 5, 6, 6, 7, 4,
                8, 9, 10, 10, 11, 8,
                12, 13, 14, 14, 15, 12,
                16, 17, 18, 18, 19, 16,
                20, 21, 22, 22, 23, 20
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