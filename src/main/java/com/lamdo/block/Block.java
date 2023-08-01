package com.lamdo.block;

import com.lamdo.block.util.BlockTexture;
import com.lamdo.block.util.Blockstate;
import com.lamdo.physics.AABB;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class Block {

    private String name;
    private boolean solid;
    private boolean collision;
    private Blockstate defaultBlockstate;

    public Block(String name, BlockTexture textures) {
        this.name = name;
        defaultBlockstate = new Blockstate(this, textures);
        solid = true;
        collision = true;
    }

    public Block notSolid() {
        solid = false;
        return this;
    }

    public Block noCollision() {
        collision = false;
        return this;
    }

    public Blockstate getDefaultBlockstate() {
        return defaultBlockstate;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean hasCollision() {
        return collision;
    }

    public AABB getAABB(int x, int y, int z) {
        return new AABB(new Vector3d(x, y, z), new Vector3d(x + 1, y + 1, z + 1));
    }

}
