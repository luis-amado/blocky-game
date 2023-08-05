package com.lamdo.render.shader;

import org.joml.Vector3f;

public class VoxelShader extends BasicShader {

    private int location_skycolor;

    public VoxelShader() {
        super("voxel");
    }

    @Override
    protected void getAllUniformLocations() {
        super.getAllUniformLocations();
        location_skycolor = getUniformLocation("skyColor");
    }

    public void loadSkyColor(Vector3f skyColor) {
        loadVector3(location_skycolor, skyColor);
    }
}
