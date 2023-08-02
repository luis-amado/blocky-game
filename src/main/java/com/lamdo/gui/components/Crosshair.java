package com.lamdo.gui.components;

import com.lamdo.gui.UIBlock;
import com.lamdo.gui.constraints.AspectRatioConstraint;
import com.lamdo.gui.constraints.PixelConstraint;

public class Crosshair extends TexturedUIComponent{

    public Crosshair() {
        super();
        setTexture("/textures/gui/crosshair.png");

        UIBlock crosshairBlock = new UIBlock()
                .centerX()
                .centerY()
                .width(new PixelConstraint(20))
                .height(new AspectRatioConstraint(1))
                .texture(textureUV(0, 0, 16, 16))
                .build();
        uiBlocks.add(crosshairBlock);
    }

}
