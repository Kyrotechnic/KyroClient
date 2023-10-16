package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.events.GuiChatEvent;
import me.kyroclient.hud.DraggableComponent;
import me.kyroclient.hud.impl.InventoryHUD;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryDisplay extends Module
{
    public NumberSetting x;
    public NumberSetting y;
    public ModeSetting blurStrength;

    public InventoryDisplay() {
        super("Inventory HUD", 0, Module.Category.RENDER);
        this.x = new NumberSetting("X1234", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.y = new NumberSetting("Y1234", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.blurStrength = new ModeSetting("Blur Strength", "Low", new String[] { "None", "Low", "High" });
        this.addSettings(this.x, this.y, this.blurStrength);
    }

    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (this.isToggled() && event.type.equals((Object)RenderGameOverlayEvent.ElementType.HOTBAR) && KyroClient.mc.thePlayer != null) {
            InventoryHUD.inventoryHUD.drawScreen();
        }
    }

    @Override
    public void assign()
    {
        KyroClient.inventoryDisplay = this;
    }

    @SubscribeEvent
    public void onChatEvent(final GuiChatEvent event) {
        if (!this.isToggled()) {
            return;
        }
        final DraggableComponent component = InventoryHUD.inventoryHUD;
        if (event instanceof GuiChatEvent.MouseClicked) {
            if (component.isHovered(event.mouseX, event.mouseY)) {
                component.startDragging();
            }
        }
        else if (event instanceof GuiChatEvent.MouseReleased) {
            component.stopDragging();
        }
        else if (event instanceof GuiChatEvent.Closed) {
            component.stopDragging();
        }
        else if (event instanceof GuiChatEvent.DrawChatEvent) {}
    }
}
