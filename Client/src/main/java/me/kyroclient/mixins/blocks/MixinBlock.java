package me.kyroclient.mixins.blocks;

import me.kyroclient.KyroClient;
import me.kyroclient.events.BlockBoundsEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = { Block.class }, priority = 1)
public abstract class MixinBlock
{
    @Shadow
    public abstract void setBlockBounds(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5);

    @Shadow
    public abstract AxisAlignedBB getCollisionBoundingBox(final World p0, final BlockPos p1, final IBlockState p2);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        final BlockBoundsEvent event = new BlockBoundsEvent(state.getBlock(), this.getCollisionBoundingBox(worldIn, pos, state), pos, collidingEntity);
        if (KyroClient.eventManager.post(event)) {
            return;
        }
        if (event.aabb != null && mask.intersectsWith(event.aabb)) {
            list.add(event.aabb);
        }
    }
}
