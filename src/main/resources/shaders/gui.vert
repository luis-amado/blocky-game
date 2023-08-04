#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoords;

out vec4 pass_color;
out vec2 pass_texCoords;

uniform mat4 transform;
uniform vec4 color;

void main() {
    gl_Position = transform * vec4(position, 0, 1);
    pass_color = color;
    pass_texCoords = texCoords;
}