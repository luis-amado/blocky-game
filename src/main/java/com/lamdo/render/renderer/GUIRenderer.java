package com.lamdo.render.renderer;

import com.lamdo.gui.UIBlock;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.shader.GUIShader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class GUIRenderer {

    private GUIShader shader;
    private static List<UIBlock> uiBlocks = new ArrayList<UIBlock>();

    public GUIRenderer(GUIShader shader) {
        this.shader = shader;
    }

    public void render() {
        shader.start();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for(UIBlock uiBlock: uiBlocks) {
            glBindVertexArray(uiBlock.getModel().vaoID());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            shader.setTransformationMatrix(uiBlock.getTransformationMatrix());
            shader.setColor(uiBlock.getColor());
            if(uiBlock.hasTexture()) {
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, uiBlock.getTexture().textureID());
            }
            shader.setHasTexture(uiBlock.hasTexture());
            glDrawArrays(GL_TRIANGLES, 0, uiBlock.getModel().indexCount());
        }
        shader.stop();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glDisable(GL_BLEND);

        uiBlocks.clear();
    }

    public static void processUIBlock(UIBlock block) {
        uiBlocks.add(block);
    }

}
