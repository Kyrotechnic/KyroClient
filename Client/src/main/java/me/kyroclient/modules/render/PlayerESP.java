package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.events.RenderLayersEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.combat.AntiBot;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MobRenderUtils;
import me.kyroclient.util.OutlineUtils;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.*;
import java.awt.*;

import net.minecraft.entity.player.*;

public class PlayerESP extends Module
{
    public ModeSetting mode;
    public NumberSetting opacity;
    private EntityPlayer lastRendered;

    public PlayerESP() {
        super("PlayerESP", 0, Category.RENDER);
        this.mode = new ModeSetting("Mode", "2D", new String[] { "Outline", "2D", "Chams", "Box", "Tracers" });
        this.opacity = new NumberSetting("Opacity", 255.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !PlayerESP.this.mode.is("Chams");
            }
        };
        this.addSettings(this.mode, this.opacity);
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (!this.isToggled() || (!this.mode.getSelected().equals("2D") && !this.mode.getSelected().equals("Box") && !this.mode.getSelected().equals("Tracers"))) {
            return;
        }
        final Color color = KyroClient.themeManager.getSecondaryColor();
        for (final EntityPlayer entityPlayer : KyroClient.mc.theWorld.playerEntities) {
            if (this.isValidEntity(entityPlayer) && entityPlayer != KyroClient.mc.thePlayer) {
                final String selected = this.mode.getSelected();
                switch (selected) {
                    case "2D": {
                        RenderUtils.draw2D((Entity)entityPlayer, event.partialTicks, 1.0f, color);
                        continue;
                    }
                    case "Box": {
                        RenderUtils.entityESPBox((Entity)entityPlayer, event.partialTicks, color);
                        continue;
                    }
                    case "Tracers": {
                        RenderUtils.tracerLine((Entity)entityPlayer, event.partialTicks, 1.0f, color);
                        continue;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(final RenderLayersEvent event) {
        final Color color = KyroClient.themeManager.getSecondaryColor();
        if (this.isToggled() && event.entity instanceof EntityPlayer && this.isValidEntity((EntityPlayer)event.entity) && event.entity != KyroClient.mc.thePlayer && this.mode.getSelected().equals("Outline")) {
            OutlineUtils.outlineESP(event, color);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderLiving(final RenderLivingEvent.Pre event) {
        if (this.lastRendered != null) {
            this.lastRendered = null;
            RenderUtils.disableChams();
            MobRenderUtils.unsetColor();
        }
        if (!(event.entity instanceof EntityOtherPlayerMP) || !this.mode.getSelected().equals("Chams") || !this.isToggled()) {
            return;
        }
        final Color color = RenderUtils.applyOpacity(KyroClient.themeManager.getSecondaryColor(event.entity.getEntityId()), (int)this.opacity.getValue());
        RenderUtils.enableChams();
        MobRenderUtils.setColor(color);
        this.lastRendered = (EntityPlayer)event.entity;
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onRenderLivingPost(final RenderLivingEvent.Specials.Pre event) {
        if (event.entity == this.lastRendered) {
            this.lastRendered = null;
            RenderUtils.disableChams();
            MobRenderUtils.unsetColor();
        }
    }

    private boolean isValidEntity(final EntityPlayer player) {
        return AntiBot.isValidEntity((Entity)player) && player.getHealth() > 0.0f && !player.isDead;
    }
}

