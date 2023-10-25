package me.kyroclient.mixins.gui;

import me.kyroclient.KyroClient;
import me.kyroclient.util.font.Fonts;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {
    int xOffset = 2;
    int yOffset = 2;
    List<String> list = new ArrayList<>();
    @Inject(method = "initGui", at = @At("HEAD"))
    public void init(CallbackInfo ci)
    {
        list.add(KyroClient.VERSION);
        list.addAll(KyroClient.changelog);
    }
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void draw(int x, int y, float pt, CallbackInfo ci)
    {
        int y1 = 0;
        for (String str : list)
        {
            Fonts.getPrimary().drawStringWithShadow(str, xOffset, y1 + yOffset, KyroClient.clickGui.getColor().getRGB());
            y1 += Fonts.getPrimary().getHeight();
        }
    }
}
