package me.kyroclient.mixins;

import me.kyroclient.KyroClient;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static String getClientModName()
    {
        return KyroClient.customBrand.isToggled() ? KyroClient.customBrand.clientBrand.getValue() : "Forge";
    }
}
