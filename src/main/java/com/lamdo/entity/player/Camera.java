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
                Window.toggleFullscreen();
                wasF11pressed = true;
            }
        } else {
            wasF11pressed = false;
        }

        Vector2f mousePos = Window.getMousePosition();
        yaw = (float) (mousePos.x * 0.1f);
        pitch = (float) (mousePos.y * 0.1f);

        speed += Window.getMouseDWheel() * 0.05f;
        speed = Math.max(0.05f, speed);

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
