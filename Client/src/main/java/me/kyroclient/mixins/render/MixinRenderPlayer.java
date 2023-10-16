package me.kyroclient.mixins.render;

import me.kyroclient.KyroClient;
import me.kyroclient.mixins.entity.MixinRenderLivingEntity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderPlayer.class })
public abstract class MixinRenderPlayer extends MixinRenderLivingEntity
{
    @Inject(method = { "preRenderCallback(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V" }, at = { @At("HEAD") })
    public void onPreRenderCallback(final AbstractClientPlayer entitylivingbaseIn, final float partialTickTime, final CallbackInfo ci) {
        if (KyroClient.giants.isToggled() && KyroClient.giants.players.isEnabled()) {
            GlStateManager.scale(KyroClient.giants.scale.getValue(), KyroClient.giants.scale.getValue(), KyroClient.giants.scale.getValue());
        }
    }
}
