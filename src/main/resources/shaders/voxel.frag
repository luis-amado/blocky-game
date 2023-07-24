#version 330 core

in vec2 pass_coords;
out vec4 fragColor;

void main() {
    fragColor = vec4(pass_coords, 1.0, 1.0);
}