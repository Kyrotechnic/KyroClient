package me.kyroclient.mixins;

import me.kyroclient.KyroClient;
import me.kyroclient.events.KeyboardEvent;
import me.kyroclient.events.LeftClickEvent;
import me.kyroclient.events.PostGuiOpenEvent;
import me.kyroclient.events.RightClickEvent;
import me.kyroclient.modules.combat.Aura;
import me.kyroclient.modules.player.ServerRotations;
import me.kyroclient.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow public EntityPlayerSP thePlayer;

    @Shadow public GuiScreen currentScreen;

    @Shadow public GameSettings gameSettings;

    @Shadow public boolean inGameHasFocus;

    @Shadow public MovingObjectPosition objectMouseOver;

    @Shadow private Entity renderViewEntity;

    @Shadow public PlayerControllerMP playerController;

    @Shadow public WorldClient theWorld;

    @Shadow private int leftClickCounter;

    @Shadow private int rightClickDelayTimer;

    @Inject(method = "startGame", at = @At("HEAD"))
    public void startGame(CallbackInfo ci)
    {
        KyroClient.init();
    }

    @Inject(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V") })
    public void keyPresses(final CallbackInfo ci) {
        final int k = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
        final char aChar = Keyboard.getEventCharacter();
        if (Keyboard.getEventKeyState()) {
            if (MinecraftForge.EVENT_BUS.post(new KeyboardEvent(k, aChar))) {
                return;
            }
            if (KyroClient.mc.currentScreen == null) {
                KyroClient.handleKey(k);
            }
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("RETURN"))
    public void sendClick(CallbackInfo ci)
    {
        final boolean click = this.currentScreen == null && gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus;
        if (KyroClient.cropNuker.isToggled() && click && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            int type = KyroClient.cropNuker.nukerMode.getIndex();

            if (type == 0) return;

            try {
                int count = KyroClient.cropNuker.roll();

                for (int i = 0; i < count; i++) {
                    boolean b = blockNuker();
                    if (!b) break;
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @Inject(method = { "displayGuiScreen" }, at = { @At("RETURN") })
    public void onGuiOpen(final GuiScreen i, final CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PostGuiOpenEvent(i));
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    public void clickMouse(CallbackInfo ci)
    {
        if (MinecraftForge.EVENT_BUS.post(new LeftClickEvent()))
        {
            ci.cancel();
        }

        if (KyroClient.delays.isToggled())
        {
            this.leftClickCounter = (int) KyroClient.delays.hitDelay.getValue();
        }
    }

    @Inject(method = { "getRenderViewEntity" }, at = { @At("HEAD") })
    public void getRenderViewEntity(final CallbackInfoReturnable<Entity> cir) {
        if (!ServerRotations.getInstance().isToggled() || this.renderViewEntity == null || this.renderViewEntity != KyroClient.mc.thePlayer) {
            return;
        }
        if (!ServerRotations.getInstance().onlyKillAura.isEnabled() || Aura.target != null) {
            ((EntityLivingBase)this.renderViewEntity).rotationYawHead = ((PlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
            ((EntityLivingBase)this.renderViewEntity).renderYawOffset = ((PlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    public void rightClick(CallbackInfo ci)
    {
        if (MinecraftForge.EVENT_BUS.post(new RightClickEvent()))
        {
            ci.cancel();
        }

        if (KyroClient.fastPlace.isToggled())
            rightClickDelayTimer = (int) KyroClient.fastPlace.placeDelay.getValue();
    }

    private boolean blockNuker()
    {
        BlockPos prev = objectMouseOver.getBlockPos();
        this.objectMouseOver = this.renderViewEntity.rayTrace(this.playerController.getBlockReachDistance(), 1.0f);
        BlockPos blockPos = this.objectMouseOver.getBlockPos();
        if (this.objectMouseOver == null || blockPos == null || this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || blockPos == prev || this.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air)
        {
            return false;
        }

        if (KyroClient.cropNuker.swing.isEnabled()) PlayerUtil.swingItem();
        if (KyroClient.cropNuker.packet.isEnabled()) KyroClient.mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, this.objectMouseOver.sideHit));
        else this.playerController.clickBlock(blockPos, this.objectMouseOver.sideHit);

        return true;
    }
}
