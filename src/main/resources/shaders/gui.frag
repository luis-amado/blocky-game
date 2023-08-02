#version 330 core

in vec4 pass_color;
in vec2 pass_texCoords;

out vec4 fragColor;

uniform float hasTexture;
uniform sampler2D textureSampler;

void main() {
    if(hasTexture > 0.5f) {
        vec4 textureColor = texture(textureSampler, pass_texCoords);
        fragColor = vec4(mix(pass_color.rgb, textureColor.rgb, textureColor.a), textureColor.a);
    } else {
        fragColor = pass_color;
    }
}
