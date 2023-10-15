//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\lukes\OneDrive\Desktop\deobfer\1.8.9 MAPPINGS"!

//Decompiled by Procyon!

package me.kyroclient.mixins.entity;

import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import java.util.*;
import net.minecraft.world.*;
import net.minecraft.crash.*;
import net.minecraft.util.*;
import net.minecraft.block.*;

@Mixin({ Entity.class })
public abstract class EntityMixin
{
    @Shadow
    protected UUID entityUniqueID;
    @Shadow
    public double lastTickPosX;
    @Shadow
    protected Random rand;
    @Shadow
    public int fireResistance;
    @Shadow
    public World worldObj;
    @Shadow
    protected boolean inPortal;
    @Shadow
    public float entityCollisionReduction;
    @Shadow
    public float rotationPitch;
    @Shadow
    public int ticksExisted;
    @Shadow
    public boolean noClip;
    @Shadow
    public Entity ridingEntity;
    @Shadow
    public boolean onGround;
    @Shadow
    public float fallDistance;
    @Shadow
    public float rotationYaw;
    @Shadow
    public boolean isAirBorne;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    private int fire;
    @Shadow
    public float distanceWalkedModified;
    @Shadow
    public float distanceWalkedOnStepModified;
    @Shadow
    private int nextStepDistance;
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public boolean isCollided;
    @Shadow
    public boolean isCollidedHorizontally;
    @Shadow
    public boolean isCollidedVertically;
    @Shadow
    public float stepHeight;
    @Shadow
    protected boolean isInWeb;
    @Shadow
    public double prevPosX;
    @Shadow
    public double prevPosZ;
    
    @Shadow
    public abstract float getCollisionBorderSize();
    
    @Shadow
    public abstract boolean isEntityInsideOpaqueBlock();
    
    @Shadow
    public abstract void extinguish();
    
    @Shadow
    public abstract void setFire(final int p0);
    
    @Shadow
    public abstract boolean isWet();
    
    @Shadow
    protected abstract void dealFireDamage(final int p0);
    
    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    
    @Shadow
    public abstract void moveFlying(final float p0, final float p1, final float p2);
    
    @Shadow
    public abstract UUID getUniqueID();
    
    @Shadow
    @Override
    public abstract boolean equals(final Object p0);
    
    @Shadow
    public abstract boolean isInWater();
    
    @Shadow
    public void moveEntity(final double x, final double y, final double z) {
    }
    
    @Shadow
    public abstract boolean isSprinting();
    
    @Shadow
    protected abstract boolean getFlag(final int p0);
    
    @Shadow
    public abstract void addEntityCrashInfo(final CrashReportCategory p0);
    
    @Shadow
    protected abstract void doBlockCollisions();
    
    @Shadow
    protected abstract void playStepSound(final BlockPos p0, final Block p1);
    
    @Shadow
    public abstract void setEntityBoundingBox(final AxisAlignedBB p0);
    
    @Shadow
    private void resetPositionToBB() {
    }
    
    @Shadow
    protected abstract void entityInit();
    
    @Shadow
    public abstract boolean isInLava();
    
    @Shadow
    public abstract boolean isOffsetPositionInLiquid(final double p0, final double p1, final double p2);
    
    @Shadow
    public abstract void setSneaking(final boolean p0);
    
    public void doResetPositionToBB() {
        this.resetPositionToBB();
    }
    
    public void setNextStepDistance(final int nextStepDistance) {
        this.nextStepDistance = nextStepDistance;
    }
    
    public int getNextStepDistance() {
        return this.nextStepDistance;
    }
    
    public int getFire() {
        return this.fire;
    }
    
    public void SetFire(final int fire) {
        this.fire = fire;
    }
    
    public void plusPlusFire() {
        ++this.fire;
    }
}
