package com.lamdo.gui.components;

import com.lamdo.block.Block;
import com.lamdo.gui.UIBlock;
import com.lamdo.gui.UITexture;
import com.lamdo.gui.constraints.AspectRatioConstraint;
import com.lamdo.gui.constraints.PixelConstraint;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.texture.GLTexture;

public class HotbarSlot extends TexturedUIComponent{

    public HotbarSlot(int index, Block block, UIBlock parent, GLTexture texture) {
        setTexture(texture);

        UIBlock slot = new UIBlock(parent)
                .left(new PixelConstraint(1 + index*41))
                .width(new PixelConstraint(40))
                .height(new AspectRatioConstraint(1))
                .bottom(new PixelConstraint(1))
                .texture(textureUV(0, 0, 23, 23))
                .build();


        UITexture iconTexture = VoxelModel.guiGetTextureUV(block.getDefaultBlockstate().getTextures().north());
        UIBlock icon = new UIBlock(slot)
                .left(new PixelConstraint(8))
                .top(new PixelConstraint(7))
                .bottom(new PixelConstraint(5))
                .right(new PixelConstraint(5))
                .texture(iconTexture)
                .build();

        uiBlocks.add(icon);
        uiBlocks.add(slot);

    }
}
