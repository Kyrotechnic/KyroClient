package me.kyroclient.mixins;

import me.kyroclient.KyroClient;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public class MixinFMLHandshakeMessage {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = { "<init>(Ljava/util/List;)V" }, at = { @At("RETURN") })
    public void modless(final List<ModContainer> modList, final CallbackInfo ci) {
        if (!KyroClient.modless.isToggled()) {
            return;
        }
        try {
            if (KyroClient.mc.isSingleplayer()) {
                return;
            }
        }
        catch (Exception e) {
            return;
        }
        this.modTags.entrySet().removeIf(mod -> mod.getKey().equalsIgnoreCase(KyroClient.MOD_ID));
    }
}
