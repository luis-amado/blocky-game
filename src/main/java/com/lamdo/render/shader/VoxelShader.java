package com.lamdo.render.shader;

import org.joml.Matrix4f;

public class VoxelShader extends ShaderProgram {

    public VoxelShader() {
        super("voxel");
    }

    private int location_projection;
    private int location_view;
    private int location_transform;

    @Override
    protected void getAllUniformLocations() {
        location_projection = getUniformLocation("projection");
        location_view = getUniformLocation("view");
        location_transform = getUniformLocation("transform");
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        loadMatrix(location_projection, projectionMatrix);
    }

    public void loadViewMatrix(Matrix4f viewMatrix) {
        loadMatrix(location_view, viewMatrix);
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        loadMatrix(location_transform, transformationMatrix);
    }
}
