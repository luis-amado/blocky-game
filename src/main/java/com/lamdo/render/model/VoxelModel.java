package com.lamdo.render.model;

import org.joml.Vector3f;

public class VoxelModel {

    private static int texture;
    private static int textureRows;

    private RawModel model;
    private Vector3f position;

    public VoxelModel(Vector3f position) {
        this.position = position;
    }

    public VoxelModel(RawModel model, Vector3f position) {
        this.model = model;
        this.position = position;
    }

    public static void setTexture(int textureID, int rows) {
        texture = textureID;
        textureRows = rows;
    }

    public static int getTexture() {
        return texture;
    }

    public RawModel getModel() {
        return model;
    }

    public void setModel(RawModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

}
