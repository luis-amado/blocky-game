package com.lamdo.util;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Vector3Util {

    public static Vector3i floorToInt(Vector3f v) {
        return new Vector3i((int) Math.floor(v.x), (int) Math.floor(v.y), (int) Math.floor(v.z));
    }
    public static Vector3i floorToInt(Vector3d v) {
        return new Vector3i((int) Math.floor(v.x), (int) Math.floor(v.y), (int) Math.floor(v.z));
    }

    public static Vector3f castToFloat(Vector3d v) {
        return new Vector3f((float) v.x, (float)v.y, (float)v.z);
    }

}
