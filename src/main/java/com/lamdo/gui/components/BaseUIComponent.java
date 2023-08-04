package com.lamdo.gui.components;

import com.lamdo.gui.UIBlock;
import com.lamdo.render.renderer.GUIRenderer;

import java.util.ArrayList;
import java.util.List;

public class BaseUIComponent extends UIComponent {

    protected List<UIBlock> uiBlocks;

    public BaseUIComponent() {
        uiBlocks = new ArrayList<UIBlock>();
    }

    @Override
    public void render() {
        for(UIBlock uiBlock: uiBlocks) {
            GUIRenderer.processUIBlock(uiBlock);
        }
    }

    protected void addComponent(BaseUIComponent other) {
        uiBlocks.addAll(other.uiBlocks);
    }
}
