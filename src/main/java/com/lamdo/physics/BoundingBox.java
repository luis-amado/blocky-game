package com.lamdo.physics;

import org.joml.Vector3d;
import org.joml.Vector3f;

public class BoundingBox {

    private float width;
    private float height;

    public BoundingBox(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public AABB toAABB(Vector3d position) {
        Vector3d minExtent = new Vector3d(position.x - width / 2f, position.y, position.z - width / 2f);
        Vector3d maxExtent = new Vector3d(position.x + width / 2f, position.y + height, position.z + width / 2f);
        return new AABB(minExtent, maxExtent);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
