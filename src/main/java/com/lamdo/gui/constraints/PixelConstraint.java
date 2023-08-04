package com.lamdo.gui.constraints;

import com.lamdo.render.Window;

public class PixelConstraint extends UIConstraint{

    private int pixels;

    public PixelConstraint(int pixels) {
        this.pixels = pixels;
    }

    @Override
    public float computeValue(ConstraintType type, float parentValue) {
        if(type.isHorizontal()) {
            int displayWidth = Window.getWidthInPixels();
            if(type == ConstraintType.RIGHT) {
                return 1 - (float)pixels/displayWidth;
            } else {
                return (float)pixels/displayWidth;
            }
        } else {
            int displayHeight = Window.getHeightInPixels();
            if(type == ConstraintType.TOP) {
                return 1 - (float)pixels/displayHeight;
            } else {
                return (float)pixels/displayHeight;
            }
        }
    }
}
