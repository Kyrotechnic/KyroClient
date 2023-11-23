//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\lukes\OneDrive\Desktop\deobfer\1.8.9 MAPPINGS"!

//Decompiled by Procyon!

package me.kyroclient.mixins.entity;

import me.kyroclient.KyroClient;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractClientPlayer.class })
public abstract class AbstractClientPlayerMixin extends PlayerMixin
{
    private static ResourceLocation getCape(final String uuid) {
        if (KyroClient.capeManager.capeList.containsKey(uuid))
            return KyroClient.capeManager.capeList.get(uuid).getCapeLocation();
        return null;
    }
    
    @Inject(method = { "getLocationCape" }, at = { @At("RETURN") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> cir) {
        final ResourceLocation minecons = getCape(this.getName());
        if (minecons != null) {
            cir.setReturnValue(minecons);
        }
    }
}
