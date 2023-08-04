package com.lamdo.render.model;

import com.lamdo.render.Loader;
import org.joml.Vector3f;

public class ShapeModel {
    private Vector3f position;
    private UpdateableRawModel model;
    private int glDrawMode;
    private boolean isStatic;

    public ShapeModel() {
        model = Loader.loadEmptyShapeVAO();
    }

    public void update(Vector3f position, int glDrawMode, boolean isStatic) {
        this.position = position;
        this.glDrawMode = glDrawMode;
        this.isStatic = isStatic;
    }

    public Vector3f position() {
        return position;
    }

    public UpdateableRawModel model() {
        return model;
    }

    public int glDrawMode() {
        return glDrawMode;
    }

    public boolean isStatic() {
        return isStatic;
    }
}