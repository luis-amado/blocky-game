package com.lamdo.render;

import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.UpdateableRawModel;
import com.lamdo.render.texture.GLTexture;
import com.lamdo.render.texture.TextureData;
import com.lamdo.util.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Loader {

    private static final List<Integer> vaos = new ArrayList<Integer>();
    private static final List<Integer> vbos = new ArrayList<Integer>();
    private static final List<Integer> textures = new ArrayList<Integer>();

    // used for voxels
    public static UpdateableRawModel loadToVAO(List<Float> positions, List<Float> textureCoords, List<Integer> indices) {
        int vaoID = createVAO();
        int indicesVBO = bindIndicesBuffer(ArrayUtils.toIntArray(indices), GL_DYNAMIC_DRAW);
        int positionsVBO = storeDataInAttributeList(0, 3, ArrayUtils.toFloatArray(positions), GL_DYNAMIC_DRAW);
        int textureCoordsVBO = storeDataInAttributeList(1, 2, ArrayUtils.toFloatArray(textureCoords), GL_DYNAMIC_DRAW);
        unbindVAO();
        return new UpdateableRawModel(vaoID, indices.size(), indicesVBO, positionsVBO, textureCoordsVBO);
    }

    // used for updating the model of a voxel chunk (like when placing or breaking blocks)
    public static void updateVAO(UpdateableRawModel model, List<Float> positions, List<Float> textureCoords, List<Integer> indices) {
        int vaoID = model.vaoID();
        glBindVertexArray(vaoID);

        // update indices
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getVbo0());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ArrayUtils.toIntArray(indices), GL_DYNAMIC_DRAW);

        // update positions
        glBindBuffer(GL_ARRAY_BUFFER, model.getVbo1());
        glBufferData(GL_ARRAY_BUFFER, ArrayUtils.toFloatArray(positions), GL_DYNAMIC_DRAW);

        // update texture coordinates
        glBindBuffer(GL_ARRAY_BUFFER, model.getVbo2());
        glBufferData(GL_ARRAY_BUFFER, ArrayUtils.toFloatArray(textureCoords), GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        model.setIndexCount(indices.size());
    }

    // used for voxels
    public static UpdateableRawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
        int vaoID = createVAO();
        int indicesVBO = bindIndicesBuffer(indices, GL_DYNAMIC_DRAW);
        int positionsVBO = storeDataInAttributeList(0, 3, positions, GL_DYNAMIC_DRAW);
        int textureCoordsVBO = storeDataInAttributeList(1, 2, textureCoords, GL_DYNAMIC_DRAW);
        unbindVAO();
        return new UpdateableRawModel(vaoID, indices.length, indicesVBO, positionsVBO, textureCoordsVBO);
    }

    // used for gui
    public static RawModel loadToVAOGUI(float[] positions, float[] texCoords) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions, GL_STATIC_DRAW);
        storeDataInAttributeList(1, 2, texCoords, GL_STATIC_DRAW);
        unbindVAO();
        return new RawModel(vaoID, positions.length / 2);
    }

    // used for shapes
    public static UpdateableRawModel loadEmptyShapeVAO() {
        int vaoID = createVAO();
        int positionsVBO = storeDataInAttributeList(0, 3, new float[0], GL_DYNAMIC_DRAW);
        int colorsVBO = storeDataInAttributeList(1, 4, new float[0], GL_DYNAMIC_DRAW);
        unbindVAO();
        return new UpdateableRawModel(vaoID, 0, positionsVBO, colorsVBO, 0);
    }

    public static void updateShapeVAO(UpdateableRawModel model, float[] positions, float[] colors) {
        int vaoID = model.vaoID();
        glBindVertexArray(vaoID);

        // update positions
        glBindBuffer(GL_ARRAY_BUFFER, model.getVbo0());
        glBufferData(GL_ARRAY_BUFFER, positions, GL_DYNAMIC_DRAW);

        // update colors
        glBindBuffer(GL_ARRAY_BUFFER, model.getVbo1());
        glBufferData(GL_ARRAY_BUFFER, colors, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        model.setIndexCount(positions.length / 3);
    }

    public static GLTexture loadTexture(String filepath) {
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
        return new GLTexture(textureID, data.width(), data.height());
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
        System.out.println("VAO count: " + vaos.size());
        System.out.println("VBO count: " + vbos.size());
        System.out.println("Texture count: " + textures.size());

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

    private static int storeDataInAttributeList(int attributeNumber, int coordSize, float[] data, int drawMode) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, data, drawMode);
        glVertexAttribPointer(attributeNumber, coordSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }

    private static int bindIndicesBuffer(int[] indices, int drawMode) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, drawMode);
        return vboID;
    }

    private static void unbindVAO() {
        glBindVertexArray(0);
    }

}
