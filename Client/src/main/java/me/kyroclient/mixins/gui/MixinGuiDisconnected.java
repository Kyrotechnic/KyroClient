package me.kyroclient.mixins.gui;

import me.kyroclient.modules.misc.AntiBan;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnected extends GuiScreen {
    @Shadow @Final
    private IChatComponent message;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        if(message.getUnformattedText().contains("banned")) {
            buttonList.add(new GuiButton(1, width / 2 - 100, Math.min(this.height / 2 + 12 / 2 + this.fontRendererObj.FONT_HEIGHT, this.height - 30) + 60, "Block ban packet"));
        }
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if(message.getUnformattedText().contains("banned")) {
            if (button.id == 1) {
                AntiBan.reconnect();
            }
        }
    }
}
