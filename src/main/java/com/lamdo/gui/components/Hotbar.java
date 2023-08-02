package com.lamdo.gui.components;

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

        UIBlock hotbarContainer = new UIBlock()
                .centerX().width(new PixelConstraint(9 * 40 + 10))
                .bottom(new PixelConstraint(20)).height(new PixelConstraint(42))
                .color(new Vector4f(0.2f, 0.2f, 0.2f, 1))
                .build();

        for(int i = 0; i < 9; i++) {
            UIBlock slot = new UIBlock(hotbarContainer)
                    .left(new PixelConstraint(1 + i*41))
                    .width(new PixelConstraint(40))
                    .height(new AspectRatioConstraint(1))
                    .bottom(new PixelConstraint(1))
                    .texture(textureUV(0, 0, 23, 23))
                    .build();
            uiBlocks.add(slot);
        }

        uiBlocks.add(hotbarContainer);

    }

}
