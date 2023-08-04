package com.lamdo.gui.constraints;

import com.lamdo.render.Window;

public class RelativePlusPixelsConstraint extends UIConstraint{

    private float relativeValue;
    private int pixelSum;

    public RelativePlusPixelsConstraint(float relativeValue, int pixelSum) {
        this.relativeValue = relativeValue;
        this.pixelSum = pixelSum;
    }

    @Override
    public float computeValue(ConstraintType type, float parentValue) {

        float relativeComputedValue;
        if(type.isCenter()) {
            relativeComputedValue = relativeValue / 2 * parentValue;
        } else {
            if(type == ConstraintType.RIGHT || type == ConstraintType.TOP) {
                relativeComputedValue = 1 - relativeValue * parentValue;
            } else {
                relativeComputedValue = relativeValue * parentValue;
            }
        }

        float pixelComputedValue;
        if(type.isHorizontal()) {
            int displayWidth = Window.getWidthInPixels();
            if(type == ConstraintType.RIGHT) {
                pixelComputedValue = 1 - (float)pixelSum/displayWidth;
            } else {
                pixelComputedValue = (float)pixelSum/displayWidth;
            }
        } else {
            int displayHeight = Window.getHeightInPixels();
            if(type == ConstraintType.TOP) {
                pixelComputedValue = 1 - (float)pixelSum/displayHeight;
            } else {
                pixelComputedValue = (float)pixelSum/displayHeight;
            }
        }

        return relativeComputedValue + pixelComputedValue;

    }
}
