package com.lamdo.util;

import com.lamdo.entity.player.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MathUtil {

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));

        Vector3f invCameraPos = new Vector3f();
        camera.getPosition().mul(-1, invCameraPos);
        viewMatrix.translate(invCameraPos);
        return viewMatrix;
    }

    public static Vector3f forwardVector(float yaw) {
        Vector3f forward = new Vector3f(
                (float)Math.sin(Math.toRadians(yaw)),
                0,
                (float)-Math.cos(Math.toRadians(yaw))
        );
        forward.normalize();
        return forward;
    }

    public static Vector3f rightVector(float yaw) {
        return forwardVector(yaw + 90);
    }

    public static Vector3f forwardVector(float yaw, float pitch) {
        float cosPitch = (float)Math.cos(Math.toRadians(pitch));
        Vector3f forward = new Vector3f(
                (float)(Math.sin(Math.toRadians(yaw)) * cosPitch),
                (float)-Math.sin(Math.toRadians(pitch)),
                (float)(-Math.cos(Math.toRadians(yaw)) * cosPitch)
        );
        forward.normalize();
        return forward;
    }

    public static int mod(int a, int b) {
        return (a % b + b) % b;
    }

}
