package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.events.GuiChatEvent;
import me.kyroclient.hud.DraggableComponent;
import me.kyroclient.hud.impl.InventoryHUD;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.SkyblockUtils;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.RenderUtils;
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
    public BooleanSetting customFont = new BooleanSetting("Custom Font", false);
    public LoreDisplay()
    {
        super("Lore Display", Category.RENDER);

        addSettings(
                scale,
                customFont
        );
    }

    @Override
    public void assign()
    {
        KyroClient.loreDisplay = this;
    }

    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (this.isToggled() && event.type.equals(RenderGameOverlayEvent.ElementType.HOTBAR) && KyroClient.mc.thePlayer != null) {
            render();
        }
    }

    public void render()
    {
        ItemStack item = KyroClient.mc.thePlayer.getHeldItem();
        if (item == null) return;

        List<String> lore = new ArrayList<>();

        lore.add(SkyblockUtils.getDisplayName(item));
        NBTTagList compound = item.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);

        double longest = Fonts.getPrimary().getStringWidth(lore.get(0));

        for (int i = 0; i < compound.tagCount(); i++)
        {
            String str = compound.get(i).toString();
            if (Fonts.getPrimary().getStringWidth(str) > longest)
                longest = Fonts.getPrimary().getStringWidth(str);

            lore.add(str);
        }
        int height = (lore.size()*11)+4;
        if (customFont.isEnabled())
            height = (lore.size()*Fonts.getPrimary().getHeight())+4;
        RenderUtils.drawBorderedRoundedRect(0, 0, (float) longest + 2, height, 5, 2, Color.GRAY.darker().darker().getRGB(), KyroClient.clickGui.getColor().getRGB());
        int y = 2;
        for (String str : lore)
        {
            if (customFont.isEnabled())
            {
                Fonts.getPrimary().drawStringWithShadow(str, 1, y, Color.white.getRGB());
                y += Fonts.getPrimary().getHeight();
            }
            else
            {
                KyroClient.mc.fontRendererObj.drawString(str, 1, y, Color.white.getRGB());
                y += 11;
            }

        }
    }
}
