package com.lamdo.entity.player;

import com.lamdo.render.Window;
import com.lamdo.util.MathUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjglx.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Camera {

    private Vector3f position;
    private float pitch, yaw;

    private float speed = 0.1f;
    private boolean wasF11pressed = false;
    private boolean fullscreen = false;
    private int windowXpos = 0;
    private int windowYpos = 0;
    private int windowWidth = 0;
    private int windowHeight = 0;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        pitch = 0;
        yaw = 0;
    }

    public Camera(Vector3f position, float pitch, float yaw) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void move() {
        float inputX = 0, inputY = 0, inputZ = 0;
        if(Window.isKeyPressed(GLFW_KEY_A)) {
            inputX -= 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_D)) {
            inputX += 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_S)) {
            inputZ -= 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_W)) {
            inputZ += 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            inputY -= 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_SPACE)) {
            inputY += 1;
        }

        if(Window.isKeyPressed(GLFW_KEY_F11)) {
            if(!wasF11pressed) {
                if(!fullscreen) {
                    IntBuffer xpos = BufferUtils.createIntBuffer(1);
                    IntBuffer ypos = BufferUtils.createIntBuffer(1);
                    IntBuffer width = BufferUtils.createIntBuffer(1);
                    IntBuffer height = BufferUtils.createIntBuffer(1);
                    glfwGetWindowPos(Window.getWindowHandle(), xpos, ypos);
                    glfwGetWindowSize(Window.getWindowHandle(), width, height);
                    windowXpos = xpos.get(0);
                    windowYpos = ypos.get(0);
                    windowWidth = width.get(0);
                    windowHeight = height.get(0);

                    long monitor = glfwGetMonitors().get(0);
                    GLFWVidMode mode = glfwGetVideoMode(monitor);
                    glfwSetWindowMonitor(Window.getWindowHandle(), monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate());
                    fullscreen = true;
                    glfwSwapInterval(1);
                } else {
                    glfwSetWindowMonitor(Window.getWindowHandle(), NULL, windowXpos, windowYpos, windowWidth, windowHeight, 0);
                    glfwSwapInterval(1);
                    fullscreen = false;
                }
                wasF11pressed = true;
            }
        } else {
            wasF11pressed = false;
        }

        Vector2f mousePos = Window.getMousePosition();
        yaw = (float) (mousePos.x * 0.1f);
        pitch = (float) (mousePos.y * 0.1f);

        position
            .add(MathUtil.forwardVector(yaw).mul(inputZ).mul(speed))
            .add(MathUtil.rightVector(yaw).mul(inputX).mul(speed))
            .add(new Vector3f(0, inputY * speed, 0));

    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

}
