package com.lamdo.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ShaderProgram {

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    public ShaderProgram(String filepath) {
        vertexShaderID = loadShader("/shaders/" + filepath + ".vert", GL_VERTEX_SHADER);
        fragmentShaderID = loadShader("/shaders/" + filepath + ".frag", GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programID, uniformName);
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    protected void bindAttribute(int attributeNumber, String variableName) {
        glBindAttribLocation(programID, attributeNumber, variableName);
    }

    protected void loadInt(int location, int value) {
        glUniform1i(location, value);
    }

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadVector3(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector2(int location, Vector2f vector) {
        glUniform2f(location, vector.x, vector.y);
    }

    protected void loadBoolean(int location, boolean value) {
        glUniform1f(location, value ? 1f : 0f);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        glUniformMatrix4fv(location, false, matrix.get(new float[16]));
    }

    private static int loadShader(String file, int type) {

        StringBuilder shaderSource = new StringBuilder();
        try {
            InputStream in = ShaderProgram.class.getResourceAsStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file!");
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
        return shaderID;

    }

}
