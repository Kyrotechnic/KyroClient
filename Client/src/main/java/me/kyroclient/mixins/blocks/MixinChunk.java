package me.kyroclient.mixins.blocks;

import me.kyroclient.KyroClient;
import me.kyroclient.events.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Chunk.class })
public class MixinChunk
{
    @Inject(method = { "setBlockState" }, at = { @At("HEAD") }, cancellable = true)
    private void onBlockChange(final BlockPos pos, final IBlockState state, final CallbackInfoReturnable<IBlockState> cir) {
        if (KyroClient.eventManager.post(new BlockChangeEvent(pos, state))) {
            cir.setReturnValue(state);
        }
    }
}
