package com.lamdo.gui.components;

import com.lamdo.block.Block;
import com.lamdo.block.Blocks;
import com.lamdo.gui.UIBlock;
import com.lamdo.gui.UITexture;
import com.lamdo.gui.constraints.AspectRatioConstraint;
import com.lamdo.gui.constraints.PixelConstraint;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Hotbar extends TexturedUIComponent{

    public Hotbar() {
        super();
        setTexture("/textures/gui/hotbar.png");

        Block[] hotbarBlocks = new Block[] {
                Blocks.GRASS, Blocks.DIRT, Blocks.STONE, Blocks.DEEPSLATE_BRICKS
        };
        int slots = hotbarBlocks.length;

        UIBlock hotbarContainer = new UIBlock()
                .centerX().width(new PixelConstraint(slots * 40 + slots + 1))
                .bottom(new PixelConstraint(20)).height(new PixelConstraint(42))
                .color(new Vector4f(0.2f, 0.2f, 0.2f, 1))
                .build();


        for(int i = 0; i < slots; i++) {
            HotbarSlot slot = new HotbarSlot(i, hotbarBlocks[i], hotbarContainer, texture);
            addComponent(slot);
        }

        uiBlocks.add(hotbarContainer);

    }

}
