package com.lamdo.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Entity {

    protected Vector3f position;
    protected Vector2f rotation;

    public Entity(Vector3f position, Vector2f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Entity() {
        this.position = new Vector3f();
        this.rotation = new Vector2f();
    }

    public Vector3f getPosition() {
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
