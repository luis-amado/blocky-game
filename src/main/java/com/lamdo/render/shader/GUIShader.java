package com.lamdo.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class GUIShader extends ShaderProgram{

    private int location_transform;
    private int location_color;

    public GUIShader() {
        super("gui");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transform = getUniformLocation("transform");
        location_color = getUniformLocation("color");
    }

    public void setTransformationMatrix(Matrix4f transformationMatrix) {
        loadMatrix(location_transform, transformationMatrix);
    }

    public void setColor(Vector4f color) {
        loadVector4(location_color, color);
    }
}
