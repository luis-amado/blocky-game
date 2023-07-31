package com.lamdo.physics;

import com.lamdo.util.MathUtil;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class AABB {

    private Vector3f minExtent;
    private Vector3f maxExtent;

    public AABB(Vector3f minExtent, Vector3f maxExtent) {
        this.minExtent = minExtent;
        this.maxExtent = maxExtent;
    }

    public boolean checkCollision(AABB two) {
        boolean collisionX = maxExtent.x >= two.minExtent.x && two.maxExtent.x >= minExtent.x;
        boolean collisionY = maxExtent.y >= two.minExtent.y && two.maxExtent.y >= minExtent.y;
        boolean collisionZ = maxExtent.z >= two.minExtent.z && two.maxExtent.z >= minExtent.z;
        return collisionX && collisionY && collisionZ;
    }

    public IntersectData sweptAABB(AABB b2, Vector3f vel) {
        float xInvEntry, yInvEntry, zInvEntry;
        float xInvExit, yInvExit, zInvExit;

        //find the distance between the objects in the near and far sides (xyz)

        if(vel.x > 0) {
            xInvEntry = b2.minExtent.x - maxExtent.x;
            xInvExit = b2.maxExtent.x - minExtent.x;
        } else {
            xInvEntry = b2.maxExtent.x - minExtent.x;
            xInvExit = b2.minExtent.x - maxExtent.x;
        }

        if(vel.y > 0) {
            yInvEntry = b2.minExtent.y - maxExtent.y;
            yInvExit = b2.maxExtent.y - minExtent.y;
        } else {
            yInvEntry = b2.maxExtent.y - minExtent.y;
            yInvExit = b2.minExtent.y - maxExtent.y;
        }

        if(vel.z > 0) {
            zInvEntry = b2.minExtent.z - maxExtent.z;
            zInvExit = b2.maxExtent.z - minExtent.z;
        } else {
            zInvEntry = b2.maxExtent.z - minExtent.z;
            zInvExit = b2.minExtent.z - maxExtent.z;
        }

        //find time of collision (and time of exit) in each axis
        //value between 0 and 1 that determine when the collision occured between current pos and desired position
        float xEntry, yEntry, zEntry;
        float xExit, yExit, zExit;

        if(vel.x == 0) {
            xEntry = Float.NEGATIVE_INFINITY;
            xExit = Float.POSITIVE_INFINITY;
        } else {
            xEntry = xInvEntry / vel.x;
            xExit = xInvExit / vel.x;
        }

        if(vel.y == 0) {
            yEntry = Float.NEGATIVE_INFINITY;
            yExit = Float.POSITIVE_INFINITY;
        } else {
            yEntry = yInvEntry / vel.y;
            yExit = yInvExit / vel.y;
        }

        if(vel.z == 0) {
            zEntry = Float.NEGATIVE_INFINITY;
            zExit = Float.POSITIVE_INFINITY;
        } else {
            zEntry = zInvEntry / vel.z;
            zExit = zInvExit / vel.z;
        }

        //find the earliest and latest times of collision
        float entryTime = Math.max(xEntry, Math.max(yEntry, zEntry));
        float exitTime = Math.min(xExit, Math.max(yExit, zExit));

        //check for no collision
        if(entryTime > exitTime || (xEntry < 0 && yEntry < 0 && zEntry < 0) || xEntry > 1 || yEntry > 1 || zEntry > 1) {
            return IntersectData.noCollision();
        } else {
            // there was a collision
            // we need to calculate the normal of the collided surface
            Vector3f collisionNormal;
            if (xEntry > yEntry && xEntry > zEntry) {
                if (xInvEntry < 0) {
                    collisionNormal = new Vector3f(1, 0, 0);
                } else {
                    collisionNormal = new Vector3f(-1, 0, 0);
                }
            } else if (yEntry > xEntry && yEntry > zEntry) {
                if(yInvEntry < 0) {
                    collisionNormal = new Vector3f(0, 1, 0);
                } else {
                    collisionNormal = new Vector3f(0, -1, 0);
                }
            } else {
                if(zInvEntry < 0) {
                    collisionNormal = new Vector3f(0, 0, 1);
                } else {
                    collisionNormal = new Vector3f(0, 0, -1);
                }
            }
            return new IntersectData(true, entryTime, collisionNormal);
        }


    }

    public List<Vector3i> collidingBlocks(AABB to) {
        List<Vector3i> positions = new ArrayList<Vector3i>();

        float minX = Math.min(minExtent.x, to.minExtent.x);
        float minY = Math.min(minExtent.y, to.minExtent.y);
        float minZ = Math.min(minExtent.z, to.minExtent.z);
        float maxX = (float) Math.ceil(Math.max(maxExtent.x, to.maxExtent.x));
        float maxY = (float) Math.ceil(Math.max(maxExtent.y, to.maxExtent.y));
        float maxZ = (float) Math.ceil(Math.max(maxExtent.z, to.maxExtent.z));

        // start at the minExtent and check a grid of blocks until the end bounding box
        for(float x = minX; x <= maxX; x++) {
            for(float y = minY; y <= maxY; y++) {
                for(float z = minZ; z <= maxZ; z++) {
                    int xPos = MathUtil.floorToInt(x);
                    int yPos = MathUtil.floorToInt(y);
                    int zPos = MathUtil.floorToInt(z);
                    Vector3i pos = new Vector3i(xPos, yPos, zPos);
                    positions.add(pos);
                }
            }
        }



//        for(float x = minExtent.x + offset.x; x <= Math.ceil(maxExtent.x+ offset.x); x++) {
//            for(float y = minExtent.y+ offset.y; y <= Math.ceil(maxExtent.y+ offset.y); y++) {
//                for(float z = minExtent.z+ offset.z; z <= Math.ceil(maxExtent.z+ offset.z); z++) {
//                    int xPos = MathUtil.floorToInt(x);
//                    int yPos = MathUtil.floorToInt(y);
//                    int zPos = MathUtil.floorToInt(z);
//                    Vector3i pos = new Vector3i(xPos, yPos, zPos);
//                    positions.add(pos);
//                }
//            }
//        }
        return positions;
    }

    public Vector3f getMinExtent() {
        return minExtent;
    }

    public Vector3f getMaxExtent() {
        return maxExtent;
    }

}
