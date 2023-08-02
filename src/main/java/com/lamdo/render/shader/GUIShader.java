package com.lamdo.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class GUIShader extends ShaderProgram{

    private int location_transform;
    private int location_color;
    private int location_hasTexture;

    public GUIShader() {
        super("gui");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transform = getUniformLocation("transform");
        location_color = getUniformLocation("color");
        location_hasTexture = getUniformLocation("hasTexture");
    }

    public void setTransformationMatrix(Matrix4f transformationMatrix) {
        loadMatrix(location_transform, transformationMatrix);
    }

    public void setColor(Vector4f color) {
        loadVector4(location_color, color);
    }

    public void setHasTexture(boolean hasTexture) {
        loadBoolean(location_hasTexture, hasTexture);
    }
}
