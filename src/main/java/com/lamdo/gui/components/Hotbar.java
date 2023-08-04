package com.lamdo.gui.components;

import com.lamdo.block.Block;
import com.lamdo.block.Blocks;
import com.lamdo.gui.UIBlock;
import com.lamdo.gui.constraints.PixelConstraint;
import com.lamdo.render.Window;
import com.lamdo.util.MathUtil;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Hotbar extends TexturedUIComponent{

    int selectedIndex = 0;
    int slots;
    UIBlock selectedSlotHighlight;

    private Block[] hotbarBlocks = new Block[] {
            Blocks.GRASS, Blocks.DIRT, Blocks.STONE, Blocks.STONE_BRICKS, Blocks.OAK_LOG, Blocks.OAK_PLANKS
    };

    public Block getSelectedBlock() {
        return hotbarBlocks[selectedIndex];
    }

    public Hotbar() {
        super();
        setTexture("/textures/gui/hotbar.png");

        slots = hotbarBlocks.length;

        UIBlock hotbarContainer = new UIBlock()
                .centerX().width(new PixelConstraint(slots * 40 + slots + 1))
                .bottom(new PixelConstraint(20)).height(new PixelConstraint(42))
                .color(new Vector4f(0.2f, 0.2f, 0.2f, 1))
                .build();

        selectedSlotHighlight = new UIBlock(hotbarContainer)
                .left(new PixelConstraint(selectedIndex * 41 + 1))
                .top(new PixelConstraint(1))
                .width(new PixelConstraint(40))
                .height(new PixelConstraint(40))
                .texture(textureUV(23, 0, 27, 27))
                .build();
        uiBlocks.add(selectedSlotHighlight);

        for(int i = 0; i < slots; i++) {
            HotbarSlot slot = new HotbarSlot(i, hotbarBlocks[i], hotbarContainer, texture);
            addComponent(slot);
        }

        uiBlocks.add(hotbarContainer);

    }

    public void update() {
        for(int i = 0; i < Math.min(slots, 9); i++) {
            if(Window.isKeyPressed(GLFW_KEY_1 + i)) {
                setSelectedIndex(i);
                return;
            }
        }

        int newIndex = selectedIndex;
        newIndex -= Math.floor(Window.getMouseDWheel());
        newIndex = MathUtil.mod(newIndex, slots);
        setSelectedIndex(newIndex);
    }

    private void setSelectedIndex(int selectedIndex) {
        if(this.selectedIndex == selectedIndex) return;
        this.selectedIndex = selectedIndex;
        selectedSlotHighlight.left(new PixelConstraint(selectedIndex * 41 + 1));
    }



}
