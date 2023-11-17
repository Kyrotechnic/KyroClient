package me.kyroclient.mixins.gui;

import me.kyroclient.KyroClient;
import me.kyroclient.events.ScoreboardRenderEvent;
import me.kyroclient.managers.ThemeManager;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
    @Shadow
    public abstract FontRenderer getFontRenderer();

    @Inject(method = { "renderScoreboard" }, at = { @At("HEAD") }, cancellable = true)
    public void renderScoreboard(final ScoreObjective s, final ScaledResolution score, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new ScoreboardRenderEvent(s, score))) {
            ci.cancel();
        }
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = false)
    public void renderHotbar(ScaledResolution sr, float pt, CallbackInfo ci)
    {
        if (!KyroClient.hud.isToggled()) return;

        int height = sr.getScaledHeight() - 22;
        int width = sr.getScaledWidth() - 4;
        GlStateManager.pushMatrix();
        RenderUtils.drawBorderedRoundedRect(2, height, width, sr.getScaledHeight(), 5, 3, KyroClient.themeManager.getPrimaryColor().getRGB(), KyroClient.themeManager.getSecondaryColor().getRGB());
        GlStateManager.popMatrix();
    }
}
