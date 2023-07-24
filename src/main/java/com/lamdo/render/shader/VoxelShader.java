package com.lamdo.render.shader;

import org.joml.Matrix4f;

public class VoxelShader extends ShaderProgram {

    public VoxelShader() {
        super("voxel");
    }

    private int location_projection;
    private int location_view;

    @Override
    protected void getAllUniformLocations() {
        location_projection = getUniformLocation("projection");
        location_view = getUniformLocation("view");
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        loadMatrix(location_projection, projectionMatrix);
    }

    public void loadViewMatrix(Matrix4f viewMatrix) {
        loadMatrix(location_view, viewMatrix);
    }
}
