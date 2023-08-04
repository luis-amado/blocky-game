package com.lamdo.physics;

import com.lamdo.block.util.Blockstate;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class IntersectData {

    private boolean intersecting;
    private double collisionTime;
    private Vector3d collisionNormal;
    private Blockstate blockstate;
    private Vector3i blockpos;

    public IntersectData(boolean intersecting, double time, Vector3d normal) {
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
        return new IntersectData(false, 1f, new Vector3d());
    }

    public boolean isIntersecting() {
        return intersecting;
    }

    public double getCollisionTime() {
        return collisionTime;
    }

    public Vector3d getCollisionNormal() {
        return collisionNormal;
    }

}
