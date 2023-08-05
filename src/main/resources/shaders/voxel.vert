#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 coords;

out vec2 pass_coords;
out float visibility;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transform;

// fog constants
const float density = 0.01;
const float gradient = 5.0;

void main() {
    vec4 worldPosition = transform * vec4(position, 1.0);
    vec4 posRelativeToCam = view * worldPosition;
    gl_Position = projection * posRelativeToCam;
    pass_coords = coords;

    float distance = length(posRelativeToCam.xyz);
    visibility = exp(-pow(distance * density, gradient));
    visibility = clamp(visibility, 0, 1);
}