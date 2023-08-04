package com.lamdo.block.util;

import com.lamdo.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Blockstate {

    private static List<Blockstate> blockstates = new ArrayList<Blockstate>();
    private static short blockstateCount = 0;

    private short id;
    private Block block;
    private BlockTexture textures;

    public Blockstate(Block block, BlockTexture textures) {
        this.block = block;
        this.textures = textures;
        this.id = blockstateCount++;
        blockstates.add(this);
    }

    public short getID() {
        return id;
    }

    public Block getBlock() {
        return block;
    }

    public BlockTexture getTextures() {
        return textures;
    }

    public static Blockstate fromID(short id) {
        return blockstates.get(id);
    }

}