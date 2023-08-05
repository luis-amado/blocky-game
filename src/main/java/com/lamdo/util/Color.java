package com.lamdo.util;

import org.joml.Vector3f;

public class Color {

    public static Vector3f fromHexCode(String code) {
        if(code.length() != 7) throw new IllegalArgumentException();
        if(code.charAt(0) != '#') throw new IllegalArgumentException();
        int r = Integer.parseInt(code.substring(1, 3), 16);
        int g = Integer.parseInt(code.substring(3, 5), 16);
        int b = Integer.parseInt(code.substring(5, 7), 16);
        return new Vector3f(r/255f, g/255f, b/255f);
    }

}
