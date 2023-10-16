package me.kyroclient.util;

import me.kyroclient.KyroClient;
import me.kyroclient.mixins.PlayerSPAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Random;

import static me.kyroclient.KyroClient.mc;

public class RotationUtils
{
    public static float lastLastReportedPitch;

    private RotationUtils() {
    }

    public static Rotation getClosestRotation(final AxisAlignedBB aabb) {
        return getRotations(getClosestPointInAABB(mc.thePlayer.getPositionEyes(1.0f), aabb));
    }

    public static Rotation getClosestRotation(final AxisAlignedBB aabb, final float offset) {
        return getClosestRotation(aabb.expand((double)(-offset), (double)(-offset), (double)(-offset)));
    }

    public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f2 = -MathHelper.cos(-pitch * 0.017453292f);
        return new Vec3(MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * f2, MathHelper.sin(-pitch * 0.017453292f), MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * f2);
    }

    public static Vec3 getLook(final Vec3 vec) {
        final double diffX = vec.xCoord - mc.thePlayer.posX;
        final double diffY = vec.yCoord - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        return getVectorForRotation((float)(-(MathHelper.atan2(diffY, dist) * 180.0 / 3.141592653589793)), (float)(MathHelper.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0));
    }

    public static Rotation getRotations(final EntityLivingBase target) {
        return getRotations(target.posX, target.posY + target.getEyeHeight() / 2.0, target.posZ);
    }

    public static Rotation getRotations(final EntityLivingBase target, final float random) {
        return getRotations(target.posX + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.posY + target.getEyeHeight() / 2.0 + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.posZ + (new Random().nextInt(3) - 1) * random * new Random().nextFloat());
    }

    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), getAngleDifference(a.getPitch(), b.getPitch()));
    }

    public static Rotation getRotations(final Vec3 vec3) {
        return getRotations(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public static Rotation getRotations(final double posX, final double posY, final double posZ) {
        final double x = posX - mc.thePlayer.posX;
        final double y = posY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double z = posZ - mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getSmoothRotation(final Rotation current, final Rotation target, final float smooth) {
        return new Rotation(current.getYaw() + (target.getYaw() - current.getYaw()) / smooth, current.getPitch() + (target.getPitch() - current.getPitch()) / smooth);
    }

    public static Rotation getLastReportedRotation() {
        return new Rotation(((PlayerSPAccessor) mc.thePlayer).getLastReportedYaw(), ((PlayerSPAccessor) mc.thePlayer).getLastReportedPitch());
    }

    public static Rotation getPlayerRotation() {
        return new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    public static Rotation getLimitedRotation(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        return new Rotation(currentRotation.getYaw() + MathHelper.clamp_float(getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw()), -turnSpeed, turnSpeed), currentRotation.getPitch() + MathHelper.clamp_float(getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch()), -turnSpeed, turnSpeed));
    }

    public static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static Rotation getBowRotation(final Entity entity) {
        final double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        final double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = mc.thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        final double xMulti = d / 0.8 * xDelta;
        final double zMulti = d / 0.8 * zDelta;
        final double x = entity.posX + xMulti - mc.thePlayer.posX;
        final double z = entity.posZ + zMulti - mc.thePlayer.posZ;
        final double y = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = mc.thePlayer.getDistanceToEntity(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        final float pitch = (float)(-(Math.atan2(y, d2) * 180.0 / 3.141592653589793)) + (float)dist * 0.11f;
        return new Rotation(yaw, -pitch);
    }

    public static Vec3 getClosestPointInAABB(final Vec3 vec3, final AxisAlignedBB aabb) {
        return new Vec3(clamp(aabb.minX, aabb.maxX, vec3.xCoord), clamp(aabb.minY, aabb.maxY, vec3.yCoord), clamp(aabb.minZ, aabb.maxZ, vec3.zCoord));
    }

    private static double clamp(final double min, final double max, final double value) {
        return Math.max(min, Math.min(max, value));
    }
}
