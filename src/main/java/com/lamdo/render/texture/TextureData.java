package com.lamdo.render.texture;

import java.nio.ByteBuffer;

public record TextureData(int width, int height, ByteBuffer buffer) {
}
