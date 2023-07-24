package com.lamdo.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private final long window;

    public Window() {

        // Initialize the GLFW library
        if(!glfwInit()) {
            System.err.println("GLFW could not be initialized.");
            System.exit(-1);
        }

        // Configure the window
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(600, 600, "LuisCraft", NULL, NULL);
        if(window == NULL) {
            glfwTerminate();
            System.err.println("There was a problem creating the window.");
            System.exit(-1);
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        GL.createCapabilities();

    }

    public void update() {
        // Swap the color buffers to render the next frame
        glfwSwapBuffers(window);

        // Poll for window events like key callbacks
        glfwPollEvents();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void terminate() {
        glfwTerminate();
    }

    public float getAspectRatio() {
        final IntBuffer width = BufferUtils.createIntBuffer(1);
        final IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        return (float)width.get(0)/height.get(0);
    }

}
