//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\lukes\OneDrive\Desktop\deobfer\1.8.9 MAPPINGS"!

//Decompiled by Procyon!

package me.kyroclient.mixins.entity;

import me.kyroclient.KyroClient;
import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.stats.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

@Mixin({ EntityPlayer.class })
public abstract class PlayerMixin extends EntityLivingBaseMixin
{
    @Shadow
    public PlayerCapabilities capabilities;
    @Shadow
    private int itemInUseCount;
    @Shadow
    public InventoryPlayer inventory;
    @Shadow
    protected float speedInAir;
    @Shadow
    public float experience;
    @Shadow
    public int experienceLevel;
    @Shadow
    public int experienceTotal;
    @Shadow
    public float eyeHeight;
    @Shadow
    protected float speedOnGround;
    @Shadow
    protected FoodStats foodStats;
    @Shadow
    public float renderOffsetX;
    @Shadow
    public float renderOffsetY;
    @Shadow
    public EntityFishHook fishEntity;
    private boolean wasSprinting;
    
    @Shadow
    public abstract void triggerAchievement(final StatBase p0);
    
    @Shadow
    public abstract void addExhaustion(final float p0);
    
    @Shadow
    public abstract FoodStats getFoodStats();
    
    @Shadow
    public abstract void attackTargetEntityWithCurrentItem(final Entity p0);
    
    @Shadow
    public abstract ItemStack getHeldItem();
    
    @Shadow
    public abstract ItemStack getCurrentEquippedItem();
    
    @Shadow
    public abstract void destroyCurrentEquippedItem();
    
    @Shadow
    protected void updateEntityActionState() {
    }
    
    @Shadow
    public abstract boolean isUsingItem();
    
    @Shadow
    public abstract ItemStack getItemInUse();
    
    @Shadow
    protected abstract String getSwimSound();
    
    @Shadow
    protected abstract boolean canTriggerWalking();
    
    @Shadow
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }
    
    @Shadow
    public abstract boolean attackEntityFrom(final DamageSource p0, final float p1);
    
    @Shadow
    protected abstract void entityInit();
    
    @Shadow
    public void moveEntityWithHeading(final float strafe, final float forward) {
    }
    
    @Shadow
    public abstract void addMovementStat(final double p0, final double p1, final double p2);
    
    @Shadow
    public abstract void onUpdate();
    
    @Shadow
    public abstract float getAIMoveSpeed();
    
    @Shadow
    public abstract EntityPlayer.EnumStatus trySleep(final BlockPos p0);
    
    public float getCollisionBorderSize() {
        return KyroClient.hitBoxes.isToggled() ? ((float)KyroClient.hitBoxes.expand.getValue()) : 0.1f;
    }
}
