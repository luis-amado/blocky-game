package com.lamdo.util;

import java.util.List;

public class ArrayUtils {

    public static int[] toIntArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        int i = 0;

        for(Integer val: list) {
            arr[i++] = val;
        }
        return arr;
    }

    public static float[] toFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        int i = 0;

        for(Float val: list) {
            arr[i++] = (val != null ? val : Float.NaN);
        }
        return arr;
    }

    public static void addIntArrayToList(int[] arr, List<Integer> list) {
        for(int i: arr) {
            list.add(i);
        }
    }

    public static void addFloatArrayToList(float[] arr, List<Float> list) {
        for(float f: arr) {
            list.add(f);
        }
    }

}
