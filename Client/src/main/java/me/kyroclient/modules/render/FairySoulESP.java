package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.util.SkyblockUtils;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class FairySoulESP extends Module {
    public BooleanSetting tracers;
    public BooleanSetting box;
    public FairySoulESP()
    {
        super("Fairy Soul ESP", Category.RENDER);

        addSettings(
                box = new BooleanSetting("Box", true),
                tracers = new BooleanSetting("Tracers", true)
        );
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e)
    {
        if (!isToggled()) return;
        if (!box.isEnabled() && !box.isEnabled()) return;

        final Color color = KyroClient.clickGui.getColor();
        for (EntityArmorStand entity : KyroClient.mc.theWorld.getEntities(EntityArmorStand.class, z -> SkyblockUtils.isFairySoul(z)))
        {
            BlockPos pos = new BlockPos(entity.posX, entity.posY + 2, entity.posZ);
            if (box.isEnabled())
                RenderUtils.blockBox(pos, color);
            if (tracers.isEnabled())
                RenderUtils.tracerLine(entity, e.partialTicks, 1.0f, color);
        }
    }
}
