//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\lukes\OneDrive\Desktop\deobfer\1.8.9 MAPPINGS"!

//Decompiled by Procyon!

package me.kyroclient.mixins.entity;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;

@Mixin({ AbstractClientPlayer.class })
public abstract class AbstractClientPlayerMixin extends PlayerMixin
{
    /*private static ResourceLocation getCape(final String uuid) {
        return OringoClient.capes.get(DigestUtils.sha256Hex(uuid));
    }
    
    @Inject(method = { "getLocationCape" }, at = { @At("RETURN") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> cir) {
        final ResourceLocation minecons = getCape(((AbstractClientPlayer)this).getUniqueID().toString());
        if (minecons != null) {
            cir.setReturnValue((Object)minecons);
        }
    }*/
}
