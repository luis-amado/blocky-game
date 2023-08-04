package com.lamdo.block.util;

import com.lamdo.util.Direction;

public record BlockTexture(int north, int south, int east, int west, int up, int down) {

    public static BlockTexture cubeAll(int all) {
        return new BlockTexture(all, all, all, all, all, all);
    }

    public static BlockTexture cubeSideUpDown(int side, int up, int down) {
        return new BlockTexture(side, side, side, side, up, down);
    }

    public static BlockTexture cubeSideEnd(int side, int end) {
        return new BlockTexture(side, side, side, side, end, end);
    }

    public int getFaceTexture(Direction face) {
        switch(face) {
            case NORTH: return north;
            case SOUTH: return south;
            case EAST: return east;
            case WEST: return west;
            case UP: return up;
            case DOWN: return down;
            default: return north;
        }
    }

}
