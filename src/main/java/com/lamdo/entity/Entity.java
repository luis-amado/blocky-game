package com.lamdo.entity;

import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class Entity {

    protected Vector3d position;
    protected Vector2f rotation;

    public Entity(Vector3d position, Vector2f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Entity() {
        this.position = new Vector3d();
        this.rotation = new Vector2f();
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public float getPitch() {
        return rotation.x;
    }

    public float getYaw() {
        return rotation.y;
    }

    protected void setPitch(float newPitch) {
        rotation.x = newPitch;
    }

    protected void setYaw(float newYaw) {
        rotation.y = newYaw;
    }

}
