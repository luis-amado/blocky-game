#version 330 core

in vec4 pass_color;

out vec4 fragColor;

void main() {
    fragColor = pass_color;
}