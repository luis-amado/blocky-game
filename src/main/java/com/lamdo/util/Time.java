package com.lamdo.util;

import static org.lwjgl.glfw.GLFW.*;

public class Time {

    private static double lastFrame;
    private static double deltaTime;

    public static void init() {
        lastFrame = glfwGetTime();
    }

    public static void updateTime() {
        double currentFrame = glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

}
