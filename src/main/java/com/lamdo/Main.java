package com.lamdo;

import com.lamdo.render.Loader;
import com.lamdo.render.Window;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.shader.VoxelShader;

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
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                 0.5f, -0.5f, 0.0f,
                 0.5f,  0.5f, 0.0f
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
        VoxelShader shader = new VoxelShader();

        VoxelModel.setTexture(Loader.loadTexture("/textures/dirt.png"), 1);

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);

            shader.start();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, VoxelModel.getTexture());
            glBindVertexArray(model.vaoID());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glDrawElements(GL_TRIANGLES, model.indexCount(), GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
            shader.stop();

            window.update();
        }

        window.terminate();

    }
}