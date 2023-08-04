package com.lamdo.gui.constraints;

import com.lamdo.gui.UIBlock;

public abstract class UIConstraint {

    protected UIBlock uiBlock;

    public void setUiBlock(UIBlock uiBlock) {
        this.uiBlock = uiBlock;
    }

    // Fix the value to a coordinate space between 0 and 1, rather than -1 and 1
    public float value(ConstraintType type, float parentValue) {
        float computedValue = computeValue(type, parentValue);
        if(type.isStatic()) {
            return computedValue * 2 - 1;
        } else if (type.isCenter()) {
            return computedValue;
        }
        return computedValue * 2;
    }

    public float value(ConstraintType type) {
        return value(type, 1);
    }

    public abstract float computeValue(ConstraintType type, float parentValue);

}
