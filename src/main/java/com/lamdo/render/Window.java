package com.lamdo.render;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private static long window;
    private static boolean wasResized;
    private static boolean fullscreen = false;
    private static int xpos, ypos, width, height;

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

        // Setup the resize callback
        glfwSetWindowSizeCallback(window, this::windowResized);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        GL.createCapabilities();

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPos(window, 0, 0);
        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);

    }

    public static void toggleFullscreen() {
        if(!fullscreen) {
            IntBuffer xposB = org.lwjglx.BufferUtils.createIntBuffer(1);
            IntBuffer yposB = org.lwjglx.BufferUtils.createIntBuffer(1);
            IntBuffer widthB = org.lwjglx.BufferUtils.createIntBuffer(1);
            IntBuffer heightB = org.lwjglx.BufferUtils.createIntBuffer(1);
            glfwGetWindowPos(window, xposB, yposB);
            glfwGetWindowSize(window, widthB, heightB);
            xpos = xposB.get(0);
            ypos = yposB.get(0);
            width = widthB.get(0);
            height = heightB.get(0);

            long monitor = glfwGetMonitors().get(0);
            GLFWVidMode mode = glfwGetVideoMode(monitor);
            glfwSetWindowMonitor(Window.getWindowHandle(), monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate());
            fullscreen = true;
            glfwSwapInterval(1);
        } else {
            glfwSetWindowMonitor(Window.getWindowHandle(), NULL, xpos, ypos, width, height, 0);
            glfwSwapInterval(1);
            fullscreen = false;
        }
    }

    private void windowResized(long w, int width, int height) {
        glViewport(0, 0, width, height);
        wasResized = true;
    }

    public void update() {
        wasResized = false;
        // Swap the color buffers to render the next frame
        glfwSwapBuffers(window);

        // Poll for window events like key callbacks
        glfwPollEvents();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void terminate() {
        glfwSetWindowSizeCallback(window, null).free();
        glfwTerminate();
    }

    public static float getAspectRatio() {
        final IntBuffer width = BufferUtils.createIntBuffer(1);
        final IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        return (float)width.get(0)/height.get(0);
    }

    // Input managing should move to its own class
    public static boolean isKeyPressed(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public static Vector2f getMousePosition() {
        DoubleBuffer xPos = org.lwjglx.BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = org.lwjglx.BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.getWindowHandle(), xPos, yPos);
        return new Vector2f((float) xPos.get(0), (float) yPos.get(0));
    }

    public static long getWindowHandle() {
        return window;
    }

    public static boolean wasWindowResized() {
        return wasResized;
    }

}
