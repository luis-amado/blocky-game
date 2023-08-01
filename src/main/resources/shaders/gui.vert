#version 330 core

layout (location = 0) in vec2 position;

out vec4 pass_color;

uniform mat4 transform;
uniform vec4 color;

void main() {
    gl_Position = transform * vec4(position, 0, 1);
    pass_color = color;
}