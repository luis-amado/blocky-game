package com.lamdo.render.shader;

import org.joml.Matrix4f;

public class GUIShader extends ShaderProgram{

    private int location_transform;

    public GUIShader() {
        super("gui");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transform = getUniformLocation("transform");
    }

    public void setTransformationMatrix(Matrix4f transformationMatrix) {
        loadMatrix(location_transform, transformationMatrix);
    }
}
