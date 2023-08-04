package com.lamdo.gui.constraints;

public class RelativeConstraint extends UIConstraint{

    private float relativeValue;

    public RelativeConstraint(float relativeValue) {
        this.relativeValue = relativeValue;
    }

    @Override
    public float computeValue(ConstraintType type, float parentValue) {
        if(type.isCenter()) {
            return relativeValue / 2 * parentValue;
        } else {
            return relativeValue * parentValue;
        }
    }
}
