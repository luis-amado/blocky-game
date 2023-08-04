package com.lamdo.util;

import org.joml.Vector2f;

import java.util.Random;

public class Noise {

    /**
     * @param seed
     * @param x
     * @param y
     * @param scale value above 0
     * @param offset
     * @param octaves
     * @param persistance value between 0-1
     * @param lacunarity value above 1
     * @return
     */
    public static float noise2D(int seed, float x, float y, float scale, float offset, int octaves, float persistance, float lacunarity) {

        Random random = new Random(seed);
        Vector2f[] octaveOffsets = new Vector2f[octaves];

        for(int i = 0; i < octaves; i++) {
            float offsetX = random.nextFloat(-100000, 100000);
            float offsetY = random.nextFloat(-100000, 100000);
            octaveOffsets[i] = new Vector2f(offsetX, offsetY);
        }

        if(scale < 0) {
            scale = 0.0001f;
        }

        float amplitude = 1;
        float frequency = 1;
        float noiseHeight = 0;

        float limitValue = 0;

        for(int i = 0; i < octaves; i++) {
            float sampleX = (x+offset) / scale * frequency + octaveOffsets[i].x;
            float sampleY = (y+offset) / scale * frequency + octaveOffsets[i].y;

            float noiseValue = OpenSimplex2S.noise2(0, sampleX, sampleY);
            noiseHeight += noiseValue * amplitude;

            limitValue += amplitude;

            amplitude *= persistance;
            frequency *= lacunarity;
        }

        //normalize the value between 0 and 1, currently from -limitValue->limitValue
        noiseHeight = MathUtil.inverseLerp(-limitValue, limitValue, noiseHeight);

        return noiseHeight;

    }

}
