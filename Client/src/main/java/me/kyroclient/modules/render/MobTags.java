package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.combat.AntiBot;
import me.kyroclient.util.EntityUtils;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobTags extends Module {
    public MobTags()
    {
        super("Mob Tags", Category.RENDER);
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event)
    {
        if (!isToggled() || KyroClient.mc.theWorld == null || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld.loadedEntityList == null) return;

        for (Entity entity : KyroClient.mc.theWorld.getLoadedEntityList())
        {
            if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityArmorStand))
            {
                Vec3 interpolated = EntityUtils.getInterpolatedPos(entity, event.partialTicks);
                RenderUtils.renderStarredNametag(entity, entity.getDisplayName().getFormattedText(), interpolated.xCoord, interpolated.yCoord, interpolated.zCoord, 100);
            }
        }
    }
}
