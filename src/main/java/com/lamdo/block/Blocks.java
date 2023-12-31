package com.lamdo.block;

import com.lamdo.block.util.BlockTexture;

public class Blocks {

    public static final Block AIR = new Block("air", BlockTexture.cubeAll(0)).notSolid().noCollision();
    public static final Block DIRT = new Block("dirt", BlockTexture.cubeAll(0));
    public static final Block GRASS = new Block("grass", BlockTexture.cubeSideUpDown(1, 2, 0));
    public static final Block STONE = new Block("stone", BlockTexture.cubeAll(3));
    public static final Block BEDROCK = new Block("bedrock", BlockTexture.cubeAll(4));
    public static final Block STONE_BRICKS = new Block("deepslate_bricks", BlockTexture.cubeAll(5));
    public static final Block OAK_LOG = new Block("oak_log", BlockTexture.cubeSideEnd(7, 8));
    public static final Block OAK_PLANKS = new Block("oak_planks", BlockTexture.cubeAll(6));

}
