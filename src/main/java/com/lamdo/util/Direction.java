package com.lamdo.util;

import org.joml.Vector3f;
import org.joml.Vector3i;

public enum Direction {
    NORTH, SOUTH, EAST, WEST, UP, DOWN;

    public static Direction[] horizontalDirections() {
        return new Direction[]{NORTH, SOUTH, EAST, WEST};
    }

    public float[] getFaceVoxelVertices(int x, int y, int z) {
        switch(this) {
            case NORTH: return new float[]{x+1, y+1, z+0, x+1, y+0, z+0, x+0, y+0, z+0, x+0, y+1, z+0};
            case SOUTH: return new float[]{x+0, y+1, z+1, x+0, y+0, z+1, x+1, y+0, z+1, x+1, y+1, z+1};
            case EAST:  return new float[]{x+1, y+1, z+1, x+1, y+0, z+1, x+1, y+0, z+0, x+1, y+1, z+0};
            case WEST:  return new float[]{x+0, y+1, z+0, x+0, y+0, z+0, x+0, y+0, z+1, x+0, y+1, z+1};
            case UP:    return new float[]{x+0, y+1, z+0, x+0, y+1, z+1, x+1, y+1, z+1, x+1, y+1, z+0};
            case DOWN:  return new float[]{x+1, y+0, z+0, x+1, y+0, z+1, x+0, y+0, z+1, x+0, y+0, z+0};
            default:    return new float[]{};
        }
    }

    public Vector3i getNormal() {
        switch(this) {
            case NORTH: return new Vector3i(0, 0, -1);
            case SOUTH: return new Vector3i(0, 0, 1);
            case EAST: return new Vector3i(1, 0, 0);
            case WEST: return new Vector3i(-1, 0, 0);
            case UP: return new Vector3i(0, 1, 0);
            case DOWN: return new Vector3i(0, -1, 0);
            default: return new Vector3i();
        }
    }

    public static Direction fromFaceNormal(Vector3f normal) {
        if(normal.z < 0) {
            return Direction.NORTH;
        } else if (normal.z > 0) {
            return Direction.SOUTH;
        } else if (normal.x > 0) {
            return Direction.EAST;
        } else if (normal.x < 0) {
            return Direction.WEST;
        } else if (normal.y < 0) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }
}
