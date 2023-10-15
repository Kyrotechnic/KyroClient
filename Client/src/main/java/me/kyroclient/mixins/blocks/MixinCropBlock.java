package me.kyroclient.mixins.blocks;

import org.spongepowered.asm.mixin.*;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BlockCrops.class })
public abstract class MixinCropBlock extends MixinBlock
{
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void init(final CallbackInfo ci) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
}
