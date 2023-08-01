package com.lamdo.gui.constraints;

public class RelativeConstraint extends UIConstraint{

    private float relativeValue;

    public RelativeConstraint(float relativeValue) {
        this.relativeValue = relativeValue;
    }

    @Override
    public float computeValue(ConstraintType type) {
        return relativeValue;
    }
}
