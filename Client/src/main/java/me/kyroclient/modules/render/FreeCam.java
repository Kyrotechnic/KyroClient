package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MovementUtils;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreeCam extends Module
{
    private EntityOtherPlayerMP playerEntity;
    public NumberSetting speed;
    public BooleanSetting tracer;

    public FreeCam() {
        super("FreeCam", Category.RENDER);
        this.speed = new NumberSetting("Speed", 3.0, 0.1, 5.0, 0.1);
        this.tracer = new BooleanSetting("Show tracer", false);
        this.addSettings(this.speed, this.tracer);
    }

    @Override
    public void onEnable() {
        if (KyroClient.mc.theWorld != null) {
            (this.playerEntity = new EntityOtherPlayerMP((World)KyroClient.mc.theWorld, KyroClient.mc.thePlayer.getGameProfile())).copyLocationAndAnglesFrom((Entity)KyroClient.mc.thePlayer);
            this.playerEntity.onGround = KyroClient.mc.thePlayer.onGround;
            KyroClient.mc.theWorld.addEntityToWorld(-2137,this.playerEntity);
        }
    }

    @Override
    public void assign()
    {
        KyroClient.freeCam = this;
    }

    @Override
    public void onDisable() {
        if (KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null || this.playerEntity == null) {
            return;
        }
        KyroClient.mc.thePlayer.noClip = false;
        KyroClient.mc.thePlayer.setPosition(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
        KyroClient.mc.theWorld.removeEntityFromWorld(-2137);
        this.playerEntity = null;
        KyroClient.mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
    }

    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (this.isToggled()) {
            KyroClient.mc.thePlayer.noClip = true;
            KyroClient.mc.thePlayer.fallDistance = 0.0f;
            KyroClient.mc.thePlayer.onGround = false;
            KyroClient.mc.thePlayer.capabilities.isFlying = false;
            KyroClient.mc.thePlayer.motionY = 0.0;
            if (!MovementUtils.isMoving()) {
                KyroClient.mc.thePlayer.motionZ = 0.0;
                KyroClient.mc.thePlayer.motionX = 0.0;
            }
            final double speed = this.speed.getValue() * 0.1;
            KyroClient.mc.thePlayer.jumpMovementFactor = (float)speed;
            if (KyroClient.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP thePlayer = KyroClient.mc.thePlayer;
                thePlayer.motionY += speed * 3.0;
            }
            if (KyroClient.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer2 = KyroClient.mc.thePlayer;
                thePlayer2.motionY -= speed * 3.0;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (this.isToggled() && this.playerEntity != null && this.tracer.isEnabled()) {
            RenderUtils.tracerLine(this.playerEntity, event.partialTicks, 1.0f, KyroClient.themeManager.getSecondaryColor());
        }
    }

    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        if (this.isToggled()) {
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && event.packet instanceof C03PacketPlayer) {
            event.setCanceled(true);
        }
    }
}
