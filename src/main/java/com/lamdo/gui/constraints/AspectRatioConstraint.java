package com.lamdo.gui.constraints;

import com.lamdo.gui.UIBlock;
import com.lamdo.render.Window;

public class AspectRatioConstraint extends UIConstraint {

    private float aspectRatio;
    public AspectRatioConstraint(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
    @Override
    public float computeValue(ConstraintType type, float parentValue) {
        if(type == ConstraintType.WIDTH) {
            return uiBlock.calculateHeightNoAspectRatio() / 2 * aspectRatio / Window.getAspectRatio();
        } else if(type == ConstraintType.HEIGHT) {
            return uiBlock.calculateWidthNoAspectRatio() / 2 / aspectRatio * Window.getAspectRatio();
        } else {
            System.err.println("Aspect ratio constraint can only be applied to width or height");
            System.exit(-1);
        }
        return 0;
    }
}
