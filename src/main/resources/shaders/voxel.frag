#version 330 core

in vec2 pass_coords;
out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
    fragColor = texture(textureSampler, pass_coords);
}