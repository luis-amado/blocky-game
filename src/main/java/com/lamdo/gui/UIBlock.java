package com.lamdo.gui;

import com.lamdo.gui.constraints.ConstraintType;
import com.lamdo.gui.constraints.PixelConstraint;
import com.lamdo.gui.constraints.UIConstraint;
import com.lamdo.render.Loader;
import com.lamdo.render.model.RawModel;
import org.joml.Matrix4f;
import org.lwjgl.system.CallbackI;

public class UIBlock {

    private UIConstraint leftConstraint;
    private UIConstraint bottomConstraint;
    private UIConstraint topConstraint;
    private UIConstraint rightConstraint;
    private UIConstraint widthConstraint;
    private UIConstraint heightConstraint;
    private UIConstraint centerXConstraint;
    private UIConstraint centerYConstraint;

    private boolean staticHorizontal = false;
    private boolean staticVertical = false;

    public UIBlock left(UIConstraint left) {
        leftConstraint = left;
        if(rightConstraint != null) staticHorizontal = true;
        if(centerXConstraint != null || (staticHorizontal && widthConstraint != null)) horizontalError();
        return this;
    }
    public UIBlock right(UIConstraint right) {
        rightConstraint = right;
        if(leftConstraint != null) staticHorizontal = true;
        if(centerXConstraint != null || (staticHorizontal && widthConstraint != null)) horizontalError();
        return this;
    }
    public UIBlock centerX() {
        return centerX(new PixelConstraint(0));
    }
    public UIBlock centerX(UIConstraint centerX) {
        centerXConstraint = centerX;
        if(leftConstraint != null || rightConstraint != null) horizontalError();
        return this;
    }
    public UIBlock top(UIConstraint top) {
        topConstraint = top;
        if(bottomConstraint != null) staticVertical = true;
        if(centerYConstraint != null || (staticVertical && heightConstraint != null)) verticalError();
        return this;
    }
    public UIBlock bottom(UIConstraint bottom) {
        bottomConstraint = bottom;
        if(topConstraint != null) staticVertical = true;
        if(centerYConstraint != null || (staticVertical && heightConstraint != null)) verticalError();
        return this;
    }
    public UIBlock centerY() {
        return centerY(new PixelConstraint(0));
    }
    public UIBlock centerY(UIConstraint centerY) {
        centerYConstraint = centerY;
        if(topConstraint != null || bottomConstraint != null) horizontalError();
        return this;
    }
    public UIBlock width(UIConstraint width) {
        widthConstraint = width;
        if(staticHorizontal) horizontalError();
        return this;
    }
    public UIBlock height(UIConstraint height) {
        heightConstraint = height;
        if(staticVertical) verticalError();
        return this;
    }

    private void horizontalError() {
        System.err.println("Conflicting constraints. You can't specify a left, right and width or center");
        System.exit(-1);
    }

    private void verticalError() {
        System.err.println("Conflicting constraints. You can't specify a top, bottom and height or center");
        System.exit(-1);
    }

    private void constraintConfigurationError() {
        System.err.println("Could not calculate the position of the UI block due to an invalid configuration of constraints.");
        System.exit(-1);
    }

    public Matrix4f getTransformationMatrix() {
        Matrix4f transformationMatrix = new Matrix4f();

        // calculate position and scale based on constraints
        float left; // could be given or could be calculated based on right and width or based on centerx and width
        if(leftConstraint != null) {
            left = leftConstraint.value(ConstraintType.LEFT);
        } else if(centerXConstraint != null) {
            if(widthConstraint == null) constraintConfigurationError();
            left = centerXConstraint.value(ConstraintType.CENTERX) * 2 - (widthConstraint.value(ConstraintType.WIDTH) / 2);
        } else {
            if(rightConstraint == null || widthConstraint == null) constraintConfigurationError();
            left = rightConstraint.value(ConstraintType.RIGHT) - widthConstraint.value(ConstraintType.WIDTH);
        }
        float bottom; // could be given or could be calculared based on top and height or based on centery and width
        if(bottomConstraint != null) {
            bottom = bottomConstraint.value(ConstraintType.BOTTOM);
        } else if (centerYConstraint != null) {
            if(heightConstraint == null) constraintConfigurationError();
            bottom = centerYConstraint.value(ConstraintType.CENTERY) * 2 - (heightConstraint.value(ConstraintType.HEIGHT) / 2);
        } else {
            if(topConstraint == null || heightConstraint == null) constraintConfigurationError();
            bottom = topConstraint.value(ConstraintType.TOP) - heightConstraint.value(ConstraintType.HEIGHT);
        }
        float width; // could be given or could be calculated based on left and right
        if(widthConstraint != null) {
            width = widthConstraint.value(ConstraintType.WIDTH);
        } else {
            if(leftConstraint == null || rightConstraint == null) constraintConfigurationError();
            width = rightConstraint.value(ConstraintType.RIGHT) - leftConstraint.value(ConstraintType.LEFT);
        }
        float height; // could be given or could be calculated based on top and bottom
        if(heightConstraint != null) {
            height = heightConstraint.value(ConstraintType.HEIGHT);
        } else {
            if(topConstraint == null || bottomConstraint == null) constraintConfigurationError();
            height = topConstraint.value(ConstraintType.TOP) - bottomConstraint.value(ConstraintType.BOTTOM);
        }

        transformationMatrix.translate(left, bottom, 0);
        transformationMatrix.scale(width, height, 0);
        return transformationMatrix;
    }

    public static RawModel uiBlockModel = Loader.loadToVAO(new float[] {
            0, 1, 0, 0, 1, 0,
            1, 0, 1, 1, 0, 1
    });


}
