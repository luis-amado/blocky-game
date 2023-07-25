package com.lamdo.render.model;

import org.joml.Vector3f;

public record ShapeModel(Vector3f position, RawModel model, int glDrawMode, boolean isStatic){}