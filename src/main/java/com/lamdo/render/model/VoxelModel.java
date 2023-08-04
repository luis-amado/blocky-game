package com.lamdo.render.model;

import com.lamdo.gui.UITexture;
import org.joml.Vector3f;

public class VoxelModel {

    private static int texture;
    private static int textureRows;

    private UpdateableRawModel model;
    private Vector3f position;

    public VoxelModel(Vector3f position) {
        this.position = position;
    }

    public VoxelModel(UpdateableRawModel model, Vector3f position) {
        this.model = model;
        this.position = position;
    }

    public boolean hasModel() {
        return model != null;
    }

    public static float[] getTextureCoords(int index) {
        int x = index % textureRows;
        int y = index / textureRows;
        float offset = 1f/textureRows;
        float startX = offset*x;
        float startY = offset*y;
        return new float[] {startX, startY, startX, startY + offset, startX + offset, startY + offset, startX + offset, startY};
    }

    public static UITexture guiGetTextureUV(int index) {
        int x = index % textureRows;
        int y = index / textureRows;
        float offset = 1f/textureRows;
        float startX = offset*x;
        float startY = offset*y;
        return new UITexture(texture, startX, startY, offset, offset);
    }

    public static void setTexture(int textureID, int rows) {
        texture = textureID;
        textureRows = rows;
    }

    public static int getTexture() {
        return texture;
    }

    public UpdateableRawModel getModel() {
        return model;
    }

    public void setModel(UpdateableRawModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

}
