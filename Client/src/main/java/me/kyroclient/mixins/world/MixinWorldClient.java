package me.kyroclient.mixins.world;

import me.kyroclient.KyroClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityMinecart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends MixinWorld {

    @Shadow @Final private Minecraft mc;

    @Shadow @Final private Set<Entity> entityList;

    @Shadow @Final private Set<Entity> entitySpawnQueue;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean spawnEntityInWorld(Entity entityIn)
    {
        if (KyroClient.noFallingBlocks.isToggled() && entityIn instanceof EntityFallingBlock)
        {
            return false;
        }

        boolean flag = super.spawnEntityInWorld(entityIn);
        this.entityList.add(entityIn);

        if (!flag)
        {
            this.entitySpawnQueue.add(entityIn);
        }
        else if (entityIn instanceof EntityMinecart)
        {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
        }

        return flag;
    }
}
