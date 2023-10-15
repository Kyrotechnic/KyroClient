package me.kyroclient.mixins.settings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.settings.GameSettings.class)
public class GameSettings {
    @Shadow
    public boolean pauseOnLostFocus = false;

    @Inject(method = "loadOptions", at = @At("RETURN"))
    public void loadOptions(CallbackInfo ci)
    {
        pauseOnLostFocus = false;
    }
}
