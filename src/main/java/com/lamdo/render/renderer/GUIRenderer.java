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
        glBindVertexArray(UIBlock.uiBlockModel.vaoID());
        glEnableVertexAttribArray(0);
        for(UIBlock uiBlock: uiBlocks) {
            shader.setTransformationMatrix(uiBlock.getTransformationMatrix());
            glDrawArrays(GL_TRIANGLES, 0, UIBlock.uiBlockModel.indexCount());
        }
        shader.stop();
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        uiBlocks.clear();
    }

    public static void processUIBlock(UIBlock block) {
        uiBlocks.add(block);
    }

}
