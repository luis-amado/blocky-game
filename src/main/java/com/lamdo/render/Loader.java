package com.lamdo.render;

import com.lamdo.render.model.RawModel;
import com.lamdo.render.texture.TextureData;
import com.lamdo.util.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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

    public static RawModel loadToVAO(float[] positions, float[] colors) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 3, colors);
        unbindVAO();
        return new RawModel(vaoID, positions.length / 3);
    }

    public static int loadTexture(String filepath) {
        TextureData data = decodeTextureFile(filepath);
        int textureID = glGenTextures();
        textures.add(textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, data.width(), data.height(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.buffer());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);
        return textureID;
    }

    public static int loadTextureAtlas(String filepath) {
        TextureData data = decodeTextureFile(filepath);
        int textureID = glGenTextures();
        textures.add(textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, data.width(), data.height(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.buffer());
        data = halfSizeAtlas(data);
        glTexImage2D(GL_TEXTURE_2D, 1, GL_RGBA, data.width(), data.height(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.buffer());
        data = halfSizeAtlas(data);
        glTexImage2D(GL_TEXTURE_2D, 2, GL_RGBA, data.width(), data.height(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.buffer());
        data = halfSizeAtlas(data);
        glTexImage2D(GL_TEXTURE_2D, 3, GL_RGBA, data.width(), data.height(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.buffer());
        data = halfSizeAtlas(data);
        glTexImage2D(GL_TEXTURE_2D, 4, GL_RGBA, data.width(), data.height(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.buffer());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 4);

        glBindTexture(GL_TEXTURE_2D, 0);
        return textureID;
    }

    // Function to create a half resolution image of an atlas by combining every group of 4 pixels into one
    private static TextureData halfSizeAtlas(TextureData data) {
        int width = data.width() / 2;
        int height = data.height() / 2;
        ByteBuffer buffer = BufferUtils.createByteBuffer(data.buffer().capacity() / 4);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int[] indices = { x*8+y*data.width()*8, x*8+y*data.width()*8+4, x*8+y*data.width()*8+data.width()*4, x*8+y*data.width()*8+data.width()*4+4};
                int r = 0, g = 0, b = 0, a = 0;
                for(int index: indices) {
                    r += data.buffer().get(index) & 0xff;
                    g += data.buffer().get(index+1) & 0xff;
                    b += data.buffer().get(index+2) & 0xff;
                    a += data.buffer().get(index+3) & 0xff;
                }
                r /= indices.length;
                g /= indices.length;
                b /= indices.length;
                a /= indices.length;

                buffer.put((byte)r);
                buffer.put((byte)g);
                buffer.put((byte)b);
                buffer.put((byte)a);
            }
        }

        buffer.flip();

        return new TextureData(width, height, buffer);

    }

    private static TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            InputStream in = Loader.class.getResourceAsStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + " but failed.");
            System.exit(-1);
        }
        return new TextureData(width, height, buffer);
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
