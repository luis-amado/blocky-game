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

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public Vector3f getPosition() {
        Vector3f pos = new Vector3f(player.getPosition());
        return pos.add(0, player.getEyeHeight(), 0);
    }

    public float getPitch() {
        return player.getPitch();
    }

    public float getYaw() {
        return player.getYaw();
    }

}
