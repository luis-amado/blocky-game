package com.lamdo.gui.constraints;

public abstract class UIConstraint {

    // Fix the value to a coordinate space between 0 and 1, rather than -1 and 1
    public float value(ConstraintType type) {
        float computedValue = computeValue(type);
        if(type.isStatic()) {
            return computedValue * 2 - 1;
        } else if (type.isCenter()) {
            return computedValue;
        }
        return computedValue * 2;
    }

    public abstract float computeValue(ConstraintType type);

}
