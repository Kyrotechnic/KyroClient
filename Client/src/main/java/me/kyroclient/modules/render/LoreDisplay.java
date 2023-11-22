package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.events.GuiChatEvent;
import me.kyroclient.hud.DraggableComponent;
import me.kyroclient.hud.impl.InventoryHUD;
import me.kyroclient.hud.impl.ItemLoreDisplay;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.SkyblockUtils;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoreDisplay extends Module {
    public NumberSetting scale = new NumberSetting("Scale", 1, 0.5, 2, 0.1);
    public NumberSetting opacity = new NumberSetting("Opacity", 150, 0, 255, 1);
    public BooleanSetting customFont = new BooleanSetting("Custom Font", false);

    public NumberSetting x;
    public NumberSetting y;

    public ItemLoreDisplay itemLoreDisplay = new ItemLoreDisplay();

    public LoreDisplay()
    {
        super("Lore Display", Category.RENDER);

        this.x = new NumberSetting("X1234", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.y = new NumberSetting("Y1234", 100.0, -100000.0, 100000.0, 1.0E-5, a -> true);

        addSettings(
                x,
                y,
                scale,
                opacity,
                customFont
        );

        itemLoreDisplay.setPosition(this.x.getValue(), this.y.getValue());
        itemLoreDisplay.setHidden(true);
    }

    @Override
    public void assign()
    {
        KyroClient.loreDisplay = this;
    }

    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (this.isToggled() && event.type.equals(RenderGameOverlayEvent.ElementType.HOTBAR) && KyroClient.mc.thePlayer != null) {
            render(itemLoreDisplay.getX(), itemLoreDisplay.getY());
        }
    }

    @SubscribeEvent
    public void onChatEvent(final GuiChatEvent event) {
        if (!this.isToggled()) {
            return;
        }
        final ItemLoreDisplay component = itemLoreDisplay;
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

    public void render(double x, double y)
    {
        itemLoreDisplay.drawScreen();

        ItemStack item = KyroClient.mc.thePlayer.getHeldItem();
        if (item == null)
        {
            itemLoreDisplay.setHidden(true);
            itemLoreDisplay.setSize(0, 0);
            return;
        }

        itemLoreDisplay.setHidden(false);

        List<String> lore = new ArrayList<>();

        lore.add(SkyblockUtils.getDisplayName(item));
        if (item.getTagCompound() == null) return;
        if (!item.getTagCompound().hasKey("display")) return;
        NBTTagCompound c = item.getTagCompound().getCompoundTag("display");
        if (!c.hasKey("Lore"))
            return;
        NBTTagList compound = c.getTagList("Lore", 8);
        double longest = KyroClient.mc.fontRendererObj.getStringWidth(lore.get(0));

        if (customFont.isEnabled())
            longest = Fonts.getPrimary().getStringWidth(lore.get(0));

        for (int i = 0; i < compound.tagCount(); i++)
        {
            String str = compound.get(i).toString();
            str = str.substring(1, str.length()-1);
            if (customFont.isEnabled() && Fonts.getPrimary().getStringWidth(str) > longest)
                longest = Fonts.getPrimary().getStringWidth(str);
            else if (KyroClient.mc.fontRendererObj.getStringWidth(str) > longest)
                longest = KyroClient.mc.fontRendererObj.getStringWidth(str);
            lore.add(str);
        }

        int height = (lore.size()*10)+4;
        if (customFont.isEnabled())
            height = (lore.size()*(Fonts.getPrimary().getHeight()+2))+4;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale.getValue(), scale.getValue(), scale.getValue());

        itemLoreDisplay.setSize(longest, height);

        RenderUtils.drawBorderedRoundedRect((float) itemLoreDisplay.getX(), (float) itemLoreDisplay.getY(), (float) longest + 5, height, 5, 2, new Color(103, 103, 103, (int) opacity.getValue()).getRGB(), KyroClient.themeManager.getSecondaryColor().getRGB());
        int yy = 2;
        for (String str : lore)
        {
            if (customFont.isEnabled())
            {
                Fonts.getPrimary().drawStringWithShadow(str, itemLoreDisplay.getX() + 2, yy + itemLoreDisplay.getY(), Color.white.getRGB());
                yy += Fonts.getPrimary().getHeight();
            }
            else
            {
                KyroClient.mc.fontRendererObj.drawString(str, (int) (itemLoreDisplay.getX() + 2), (int) (yy + itemLoreDisplay.getY()), Color.white.getRGB());
                yy += 10;
            }

        }
        GlStateManager.popMatrix();

        this.x.setValue(itemLoreDisplay.getX());
        this.y.setValue(itemLoreDisplay.getX() + itemLoreDisplay.getHeight());
    }
}
