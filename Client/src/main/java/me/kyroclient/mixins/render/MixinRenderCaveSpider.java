package me.kyroclient.mixins.render;

import me.kyroclient.KyroClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderCaveSpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderCaveSpider.class })
public class MixinRenderCaveSpider
{
    @Inject(method = { "preRenderCallback(Lnet/minecraft/entity/monster/EntityCaveSpider;F)V" }, at = { @At("HEAD") }, cancellable = true)
    private <T extends EntityCaveSpider> void onPreRenderCallback(final T entitylivingbaseIn, final float partialTickTime, final CallbackInfo ci) {
        if (KyroClient.giants.isToggled() && KyroClient.giants.mobs.isEnabled()) {
            GlStateManager.scale(KyroClient.giants.scale.getValue(), KyroClient.giants.scale.getValue(), KyroClient.giants.scale.getValue());
        }
    }
}
