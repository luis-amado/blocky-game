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

    public static Matrix4f create3rdPersonStaticViewMatrix(Camera camera, float distance) {
        Matrix4f viewMatrix = new Matrix4f();

        float horizontalDistance = distance * (float)Math.cos(Math.toRadians(camera.getPitch()));
        float verticalDistance = distance * (float)Math.sin(Math.toRadians(camera.getPitch()));

        float theta = 180-camera.getYaw();
        float dx = horizontalDistance * (float)Math.sin(Math.toRadians(theta));
        float dz = horizontalDistance * (float)Math.cos(Math.toRadians(theta));
        Vector3f position = new Vector3f(-dx, verticalDistance, -dz);

        viewMatrix.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        viewMatrix.translate(position.negate());
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

    public static int floorToInt(float a) {
        return (int)Math.floor(a);
    }
    public static int floorToInt(double a) {
        return (int)Math.floor(a);
    }

    public static float lerp(float a, float b, float t) {
        return (1.0f - t) * a + b * t;
    }

    public static float inverseLerp(float a, float b, float v) {
        return (v - a) / (b - a);
    }

    public static float clamp01(float v) {
        if(v < 0) return 0;
        if(v > 1) return 1;
        return v;
    }

    public static float clamp(float v, float a, float b) {
        if(v < a) return a;
        if(v > b) return b;
        return v;
    }



    /**
     * Increase the magnitude of a number.
     * If the number is negative, then the number will become more negative, if it's positive it will become more positive
     * @param v
     * @param amount
     * @return the changed number
     */
    public static float changeMagnitude(float v, float amount) {
        if(v >= 0) {
            return v + amount;
        } else {
            return v - amount;
        }
    }

    /**
     * Regardless of sign, this returns the value with the smallest absolute value
     * @param a
     * @param b
     * @return
     */
    public static float minMagnitude(float a, float b) {
        float absA = Math.abs(a);
        float absB = Math.abs(b);
        return absA < absB ? a : b;
    }

    /**
     * Regardless of sign, this returns the value with the highest absolute value
     * @param a
     * @param b
     * @return
     */
    public static float maxMagnitude(float a, float b) {
        float absA = Math.abs(a);
        float absB = Math.abs(b);
        return absA > absB ? a : b;
    }

}
