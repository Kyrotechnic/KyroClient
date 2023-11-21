package me.kyroclient.mixins.gui;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.render.PopupAnimation;
import me.kyroclient.util.font.Fonts;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiContainer.class })
public abstract class MixinGuiContainer extends MixinGuiScreen {
    @Shadow
    public Container inventorySlots;

    @Inject(method = { "drawScreen" }, at = { @At("HEAD") }, cancellable = true)
    public void onDrawScreen(final int k1, final int slot, final float i1, final CallbackInfo ci) {
        if (this.inventorySlots instanceof ContainerChest && KyroClient.guiMove.shouldHideGui((ContainerChest)this.inventorySlots)) {
            this.mc.inGameHasFocus = true;
            this.mc.mouseHelper.grabMouseCursor();
            final ScaledResolution res = new ScaledResolution(this.mc);
            Fonts.getSecondary().drawSmoothCenteredStringWithShadow("In terminal!", res.getScaledWidth() / 2, res.getScaledHeight() / 2, KyroClient.themeManager.getSecondaryColor().getRGB());
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            MinecraftForge.EVENT_BUS.post((Event)new GuiScreenEvent.BackgroundDrawnEvent((GuiScreen) KyroClient.mc.currentScreen));
            ci.cancel();
        }
    }

    @Inject(method = { "drawScreen" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerBackgroundLayer(FII)V") }, cancellable = true)
    public void onDrawBackground(final int k1, final int slot, final float i1, final CallbackInfo ci) {
        if (PopupAnimation.shouldScale(KyroClient.mc.currentScreen)) {
            GL11.glPushMatrix();
            PopupAnimation.doScaling();
        }
    }

    @Inject(method = { "drawScreen" }, at = { @At("RETURN") }, cancellable = true)
    public void onDrawScreenPost(final int k1, final int slot, final float i1, final CallbackInfo ci) {
        if (PopupAnimation.shouldScale(KyroClient.mc.currentScreen)) {
            GL11.glPopMatrix();
        }
    }
}
