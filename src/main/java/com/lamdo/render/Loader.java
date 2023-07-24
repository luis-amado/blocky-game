package com.lamdo.render;

import com.lamdo.render.model.RawModel;
import com.lamdo.util.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Loader {

    private static final List<Integer> vaos = new ArrayList<Integer>();
    private static final List<Integer> vbos = new ArrayList<Integer>();
    private static final List<Integer> textures = new ArrayList<Integer>();

    public static RawModel loadToVAO(List<Float> positions, List<Float> textureCoords, List<Integer> indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(ArrayUtils.toIntArray(indices));
        storeDataInAttributeList(0, 3, ArrayUtils.toFloatArray(positions));
        storeDataInAttributeList(1, 2, ArrayUtils.toFloatArray(textureCoords));
        unbindVAO();
        return new RawModel(vaoID, indices.size());
    }

    public static RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public static int loadTexture(String filepath) {
        try (InputStream imageStream = Loader.class.getResourceAsStream(filepath)) {
            Texture texture = TextureLoader.getTexture("png", imageStream);
            int textureID = texture.getTextureID();
            textures.add(textureID);
            glBindTexture(GL_TEXTURE_2D, textureID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            glGenerateMipmap(GL_TEXTURE_2D);

            glBindTexture(GL_TEXTURE_2D, 0);
            return textureID;
        } catch (IOException e) {
            System.out.println("Texture " + filepath + " not found.");
        }
        return -1;
    }

    public static void cleanUp() {
        for(int vao: vaos) {
            glDeleteVertexArrays(vao);
        }
        for(int vbo: vbos) {
            glDeleteBuffers(vbo);
        }
        for(int texture: textures) {
            glDeleteTextures(texture);
        }
    }

    private static int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private static void storeDataInAttributeList(int attributeNumber, int coordSize, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private static void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    private static void unbindVAO() {
        glBindVertexArray(0);
    }

}
