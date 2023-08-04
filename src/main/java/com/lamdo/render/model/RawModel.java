package com.lamdo.render.model;

public class RawModel {
    private int vaoID;
    protected int indexCount;

    public RawModel(int vaoID, int indexCount) {
        this.vaoID = vaoID;
        this.indexCount = indexCount;
    }

    public int vaoID() {
        return vaoID;
    }

    public int indexCount() {
        return indexCount;
    }
}