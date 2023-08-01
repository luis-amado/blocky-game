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

        glBindVertexArray(UIBlock.uiBlockModel.vaoID());
        glEnableVertexAttribArray(0);
        for(UIBlock uiBlock: uiBlocks) {
            shader.setTransformationMatrix(uiBlock.getTransformationMatrix());
            shader.setColor(uiBlock.getColor());
            glDrawArrays(GL_TRIANGLES, 0, UIBlock.uiBlockModel.indexCount());
        }
        shader.stop();
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glDisable(GL_BLEND);

        uiBlocks.clear();
    }

    public static void processUIBlock(UIBlock block) {
        uiBlocks.add(block);
    }

}
