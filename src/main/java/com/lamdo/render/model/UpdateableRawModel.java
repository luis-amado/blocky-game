package com.lamdo.render.model;

public class UpdateableRawModel extends RawModel {

    private int vbo0;
    private int vbo1;
    private int vbo2;

    public UpdateableRawModel(int vaoID, int indexCount, int vbo0, int vbo1, int vbo2) {
        super(vaoID, indexCount);
        this.vbo0 = vbo0;
        this.vbo1 = vbo1;
        this.vbo2 = vbo2;
    }

    public int getVbo0() {
        return vbo0;
    }

    public int getVbo1() {
        return vbo1;
    }

    public int getVbo2() {
        return vbo2;
    }

    public void setIndexCount(int indexCount) {
        this.indexCount = indexCount;
    }

}
