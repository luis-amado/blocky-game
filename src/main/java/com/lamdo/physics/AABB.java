package com.lamdo.physics;

import com.lamdo.util.MathUtil;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class AABB {

    private Vector3d minExtent;
    private Vector3d maxExtent;

    public AABB(Vector3d minExtent, Vector3d maxExtent) {
        this.minExtent = minExtent;
        this.maxExtent = maxExtent;
    }

    public boolean checkCollision(AABB two) {
        boolean collisionX = maxExtent.x >= two.minExtent.x && two.maxExtent.x >= minExtent.x;
        boolean collisionY = maxExtent.y >= two.minExtent.y && two.maxExtent.y >= minExtent.y;
        boolean collisionZ = maxExtent.z >= two.minExtent.z && two.maxExtent.z >= minExtent.z;
        return collisionX && collisionY && collisionZ;
    }

    public IntersectData sweptAABB(AABB b2, Vector3d vel) {
        double xInvEntry, yInvEntry, zInvEntry;
        double xInvExit, yInvExit, zInvExit;

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
        double xEntry, yEntry, zEntry;
        double xExit, yExit, zExit;

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
        double entryTime = Math.max(xEntry, Math.max(yEntry, zEntry));
        double exitTime = Math.min(xExit, Math.max(yExit, zExit));

        //check for no collision
        if(entryTime > exitTime || (xEntry < 0 && yEntry < 0 && zEntry < 0) || xEntry > 1 || yEntry > 1 || zEntry > 1) {
            return IntersectData.noCollision();
        } else {
            // there was a collision
            // we need to calculate the normal of the collided surface
            Vector3d collisionNormal;
            if (xEntry > yEntry && xEntry > zEntry) {
                if (xInvEntry < 0) {
                    collisionNormal = new Vector3d(1, 0, 0);
                } else {
                    collisionNormal = new Vector3d(-1, 0, 0);
                }
            } else if (yEntry > xEntry && yEntry > zEntry) {
                if(yInvEntry < 0) {
                    collisionNormal = new Vector3d(0, 1, 0);
                } else {
                    collisionNormal = new Vector3d(0, -1, 0);
                }
            } else {
                if(zInvEntry < 0) {
                    collisionNormal = new Vector3d(0, 0, 1);
                } else {
                    collisionNormal = new Vector3d(0, 0, -1);
                }
            }
            return new IntersectData(true, entryTime, collisionNormal);
        }


    }

    public List<Vector3i> collidingBlocks(AABB to) {
        List<Vector3i> positions = new ArrayList<Vector3i>();

        double minX = Math.min(minExtent.x, to.minExtent.x);
        double minY = Math.min(minExtent.y, to.minExtent.y);
        double minZ = Math.min(minExtent.z, to.minExtent.z);
        double maxX = (float) Math.ceil(Math.max(maxExtent.x, to.maxExtent.x));
        double maxY = (float) Math.ceil(Math.max(maxExtent.y, to.maxExtent.y));
        double maxZ = (float) Math.ceil(Math.max(maxExtent.z, to.maxExtent.z));

        // start at the minExtent and check a grid of blocks until the end bounding box
        for(double x = minX; x <= maxX; x++) {
            for(double y = minY; y <= maxY; y++) {
                for(double z = minZ; z <= maxZ; z++) {
                    int xPos = MathUtil.floorToInt(x);
                    int yPos = MathUtil.floorToInt(y);
                    int zPos = MathUtil.floorToInt(z);
                    Vector3i pos = new Vector3i(xPos, yPos, zPos);
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    public Vector3d getMinExtent() {
        return minExtent;
    }

    public Vector3d getMaxExtent() {
        return maxExtent;
    }

}
