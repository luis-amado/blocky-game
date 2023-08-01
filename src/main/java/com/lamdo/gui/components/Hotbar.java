package com.lamdo.gui.components;

import com.lamdo.gui.UIBlock;
import com.lamdo.gui.constraints.AspectRatioConstraint;
import com.lamdo.gui.constraints.PixelConstraint;
import org.joml.Vector4f;

public class Hotbar extends BaseUIComponent{

    public Hotbar() {
        super();

        UIBlock hotbarContainer = new UIBlock()
                .centerX().width(new PixelConstraint(9 * 40 + 10 * 2))
                .bottom(new PixelConstraint(20)).height(new PixelConstraint(44));

        for(int i = 0; i < 9; i++) {
            UIBlock slot = new UIBlock(hotbarContainer)
                    .left(new PixelConstraint(2 + i*42))
                    .width(new PixelConstraint(40))
                    .height(new AspectRatioConstraint(1))
                    .bottom(new PixelConstraint(2))
                    .color(new Vector4f(0, 0, 0, 0.5f));
            uiBlocks.add(slot);
        }

        uiBlocks.add(hotbarContainer);

    }

}
