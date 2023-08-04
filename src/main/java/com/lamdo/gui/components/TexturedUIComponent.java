package com.lamdo.gui.components;

import com.lamdo.gui.UITexture;
import com.lamdo.render.Loader;
import com.lamdo.render.texture.GLTexture;
import com.lamdo.render.texture.TextureData;

public class TexturedUIComponent extends BaseUIComponent{

    protected GLTexture texture;

    protected void setTexture(String texturePath) {
        texture = Loader.loadTexture(texturePath);
    }

    protected void setTexture(GLTexture texture) {
        this.texture = texture;
    }

    protected UITexture textureUV(int x, int y, int width, int height) {
        return new UITexture(texture.texId(), (float)x / texture.texWidth(), (float)y / texture.texHeight(), (float)width / texture.texWidth(), (float)height / texture.texHeight());
    }

}
