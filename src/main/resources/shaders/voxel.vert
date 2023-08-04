#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 coords;

out vec2 pass_coords;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transform;

void main() {
    gl_Position = projection * view * transform * vec4(position, 1.0);
    pass_coords = coords;
}