#version 330 core

in vec2 pass_coords;
in float visibility;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 skyColor;

void main() {
    vec4 textureColor = texture(textureSampler, pass_coords);
    if (textureColor.a < 0.5f) {
        discard;
    } else {
        fragColor = mix(vec4(skyColor, 1), textureColor, visibility);
    }
}