package com.lamdo.physics;

import org.joml.Vector3f;

public class BoundingBox {

    private float width;
    private float height;

    public BoundingBox(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public AABB toAABB(Vector3f position) {
        Vector3f minExtent = new Vector3f(position.x - width / 2f, position.y, position.z - width / 2f);
        Vector3f maxExtent = new Vector3f(position.x + width / 2f, position.y + height, position.z + width / 2f);
        return new AABB(minExtent, maxExtent);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
