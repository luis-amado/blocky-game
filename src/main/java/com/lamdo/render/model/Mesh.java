package com.lamdo.render.model;

import java.util.List;

public record Mesh(List<Float> positions, List<Float> textureCoords, List<Integer> indices) {
}
