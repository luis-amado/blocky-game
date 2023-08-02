package com.lamdo.gui;

import com.lamdo.gui.constraints.AspectRatioConstraint;
import com.lamdo.gui.constraints.ConstraintType;
import com.lamdo.gui.constraints.PixelConstraint;
import com.lamdo.gui.constraints.UIConstraint;
import com.lamdo.render.Loader;
import com.lamdo.render.model.RawModel;
import org.joml.Matrix4f;
import org.joml.Vector4f;
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

    private Vector4f color = new Vector4f(1, 1, 1, 0);
    private UITexture texture;

    public UIBlock parent;

    private RawModel model;

    public UIBlock() {}
    public UIBlock(UIBlock parent) {
        this.parent = parent;
    }

    public UIBlock color(Vector4f color) {
        this.color = color;
        return this;
    }

    public UIBlock texture(UITexture texture) {
        this.texture = texture;
        return this;
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public UITexture getTexture() {
        return texture;
    }

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
        widthConstraint.setUiBlock(this);
        return this;
    }
    public UIBlock height(UIConstraint height) {
        heightConstraint = height;
        if(staticVertical) verticalError();
        heightConstraint.setUiBlock(this);
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
        float left = calculateLeft();
        float bottom = calculateBottom();
        float width = calculateWidth();
        float height = calculateHeight();

        transformationMatrix.translate(left, bottom, 0);
        transformationMatrix.scale(width, height, 0);
        return transformationMatrix;
    }

    private float calculateLeft() {
        float parentLeft, parentRight, parentCenterX;
        if(parent != null) {
            parentLeft = parent.calculateLeft();
            parentRight = parentLeft + parent.calculateWidth();
            parentCenterX = 1 - (parentLeft + parent.calculateWidth() / 2);
        } else {
            parentLeft = -1;
            parentRight = 1;
            parentCenterX = 0;
        }

        // the left position can be given, or has to be derived from the centerX/width or right/width
        float left;
        if(leftConstraint != null) {
            left = parentLeft + 1 + leftConstraint.value(ConstraintType.LEFT);
        } else if (centerXConstraint != null) {
            if(widthConstraint == null) constraintConfigurationError();
            left = centerXConstraint.value(ConstraintType.CENTERX) * 2 - (calculateWidth() / 2) - parentCenterX;
        } else {
            if(rightConstraint == null || widthConstraint == null) constraintConfigurationError();
            left = parentRight - (1- rightConstraint.value(ConstraintType.RIGHT)) - calculateWidth();
        }

        return left;
    }
    private float calculateBottom() {
        float parentBottom, parentTop, parentCenterY;
        if(parent != null) {
            parentBottom = parent.calculateBottom();
            parentTop = parentBottom + parent.calculateHeight();
            parentCenterY = 1 - (parentBottom + parent.calculateHeight() / 2);
        } else {
            parentBottom = -1;
            parentTop = 1;
            parentCenterY = 0;
        }

        float bottom; // could be given or could be calculared based on top and height or based on centery and width
        if(bottomConstraint != null) {
            bottom = parentBottom + 1 + bottomConstraint.value(ConstraintType.BOTTOM);
        } else if (centerYConstraint != null) {
            if(heightConstraint == null) constraintConfigurationError();
            bottom = centerYConstraint.value(ConstraintType.CENTERY) * 2 - (calculateHeight() / 2) - parentCenterY;
        } else {
            if(topConstraint == null || heightConstraint == null) constraintConfigurationError();
            bottom = parentTop - (1 - topConstraint.value(ConstraintType.TOP)) - calculateHeight();
        }
        return bottom;
    }
    private float calculateWidth() {
        float parentWidth;
        if(parent == null) {
            parentWidth = 1;
        } else {
            parentWidth = parent.calculateWidth() / 2;
        }

        float width; // could be given or could be calculated based on left and right
        if(widthConstraint != null) {
            width = widthConstraint.value(ConstraintType.WIDTH, parentWidth);
        } else {
            if(leftConstraint == null || rightConstraint == null) constraintConfigurationError();
            width = rightConstraint.value(ConstraintType.RIGHT, parentWidth) - leftConstraint.value(ConstraintType.LEFT, parentWidth);
        }
        return width;
    }
    private float calculateHeight() {
        float parentHeight;
        if(parent == null) {
            parentHeight = 1;
        } else {
            parentHeight = parent.calculateHeight() / 2;
        }

        float height; // could be given or could be calculated based on top and bottom
        if(heightConstraint != null) {
            height = heightConstraint.value(ConstraintType.HEIGHT, parentHeight);
        } else {
            if(topConstraint == null || bottomConstraint == null) constraintConfigurationError();
            height = topConstraint.value(ConstraintType.TOP, parentHeight) - bottomConstraint.value(ConstraintType.BOTTOM, parentHeight);
        }
        return height;
    }

    public float calculateWidthNoAspectRatio() {
        if(widthConstraint instanceof AspectRatioConstraint) {
            System.err.println("Aspect ratio constraint can only describe one, width or height");
            System.exit(-1);
        }
        return calculateWidth();
    }
    public float calculateHeightNoAspectRatio() {
        if(heightConstraint instanceof AspectRatioConstraint) {
            System.err.println("Aspect ratio constraint can only describe one, width or height");
            System.exit(-1);
        }
        return calculateHeight();
    }

    public UIBlock build() {
        float u, v, uu, vv;
        if(hasTexture()) {
            u = texture.u();
            v = texture.v();
            uu = texture.u() + texture.w();
            vv = texture.v() + texture.h();
        } else {
            u = 0;
            v = 0;
            uu = 1;
            vv = 1;
        }
        model = Loader.loadToVAOGUI(
                new float[] {0,  1, 0, 0,  1, 0,  1, 0,  1,  1, 0,  1},
                new float[] {u, v, u, vv, uu, vv, uu, vv, uu, v, u, v}
        );
        return this;
    }

    public Vector4f getColor() {
        return color;
    }


    public RawModel getModel() {
        return model;
    }
}
