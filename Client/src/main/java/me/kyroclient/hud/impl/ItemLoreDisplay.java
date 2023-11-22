package me.kyroclient.hud.impl;

import me.kyroclient.hud.DraggableComponent;
import me.kyroclient.hud.HudVec;

public class ItemLoreDisplay extends DraggableComponent {
    public ItemLoreDisplay()
    {
        setSize(0, 0);
    }

    @Override
    public double getY() {
        return this.y - height;
    }


    @Override
    public HudVec drawScreen() {
        if (this.isDragging()) {
            this.y = getMouseY() + this.startY;
            this.x = getMouseX() + this.startX;
        }
        return null;
    }

    @Override
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX > this.getX() && mouseX < this.getX() + this.width && mouseY > this.getY() && mouseY < this.getY() + this.height;
    }
}
