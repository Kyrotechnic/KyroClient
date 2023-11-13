package me.kyroclient.mixins;

import me.kyroclient.KyroClient;
import me.kyroclient.util.SkyblockUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerController {
    @Shadow private int blockHitDelay;

    @Shadow private float stepSoundTickCounter;

    @Shadow private float curBlockDamageMP;

    @Shadow @Final private Minecraft mc;

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"))
    private void blockHitDelay(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir)
    {
        if (KyroClient.miningQol.shouldRemoveHitDelay(blockHitDelay))
            blockHitDelay = 0;
    }

    @Redirect(method = "isHittingPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemStackTagsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean shouldTagsBeEqual(ItemStack stackA, ItemStack stackB) {
        if (KyroClient.miningQol.preventReset()) {
            return SkyblockUtils.getSkyblockItemID(stackA).equals(SkyblockUtils.getSkyblockItemID(stackB));
        }
        return ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    @Inject(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;sendBlockBreakProgress(ILnet/minecraft/util/BlockPos;I)V"))
    private void preBreakBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir){
        if (KyroClient.miningQol.shouldPreBreakBlock(this.stepSoundTickCounter, this.curBlockDamageMP)) {
            this.mc.theWorld.setBlockToAir(posBlock); // Maybe instead use: this.onPlayerDestroyBlock(posBlock, directionFacing);
            // The following is probably not required.
            this.curBlockDamageMP = 0.0F;
            this.stepSoundTickCounter = 0f;

            this.blockHitDelay = 5;
        }
    }

    @Redirect(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;)F"))
    private float tweakBlockDamage(Block instance, EntityPlayer playerIn, World worldIn, BlockPos posBlock){

        Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();

        float relHardness = block.getPlayerRelativeBlockHardness(playerIn, worldIn, posBlock);
        if (KyroClient.miningQol.shouldTweakVanilla()){
            float threshold = (float) KyroClient.miningQol.threshold.getValue();
            if (this.curBlockDamageMP + relHardness >= threshold) {
                return relHardness + 1f - threshold;
            }
        }
        return relHardness;
    }
}
