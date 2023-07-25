package com.lamdo.physics;

import com.lamdo.block.util.Blockstate;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class IntersectData {

    private boolean intersecting;
    private float collisionTime;
    private Vector3f collisionNormal;
    private Blockstate blockstate;
    private Vector3i blockpos;

    public IntersectData(boolean intersecting, float time, Vector3f normal) {
        this.intersecting = intersecting;
        this.collisionTime = time;
        this.collisionNormal = normal;
    }

    public void setBlock(Blockstate blockstate, Vector3i pos) {
        this.blockstate = blockstate;
        this.blockpos = pos;
    }

    public Blockstate getBlockstate() {
        return blockstate;
    }

    public Vector3i getBlockpos() {
        return blockpos;
    }

    public static IntersectData noCollision() {
        return new IntersectData(false, 1f, new Vector3f());
    }

    public boolean isIntersecting() {
        return intersecting;
    }

    public float getCollisionTime() {
        return collisionTime;
    }

    public Vector3f getCollisionNormal() {
        return collisionNormal;
    }

}
