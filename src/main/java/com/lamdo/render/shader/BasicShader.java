package com.lamdo.render.shader;

import org.joml.Matrix4f;

public class BasicShader extends ShaderProgram{

    private int location_view;
    private int location_transform;
    private int location_projection;

    public BasicShader(String filepath) {
        super(filepath);
    }

    @Override
    protected void getAllUniformLocations() {
        location_view = getUniformLocation("view");
        location_projection = getUniformLocation("projection");
        location_transform = getUniformLocation("transform");
    }

    public void loadViewMatrix(Matrix4f viewMatrix) {
        loadMatrix(location_view, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        loadMatrix(location_projection, projectionMatrix);
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        loadMatrix(location_transform, transformationMatrix);
    }
}
