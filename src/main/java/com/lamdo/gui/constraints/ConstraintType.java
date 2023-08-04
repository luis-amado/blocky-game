package com.lamdo.gui.constraints;

public enum ConstraintType {
    LEFT, RIGHT, TOP, BOTTOM, WIDTH, HEIGHT, CENTERX, CENTERY;

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT || this == WIDTH || this == CENTERX;
    }

    public boolean isVertical() {
        return this == TOP || this == BOTTOM || this == HEIGHT || this == CENTERY;
    }

    public boolean isStatic() {
        return this == LEFT || this == RIGHT  || this == TOP || this == BOTTOM;
    }

    public boolean isCenter() {
        return this == CENTERX || this == CENTERY;
    }
}