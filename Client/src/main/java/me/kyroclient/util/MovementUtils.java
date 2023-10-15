package me.kyroclient.util;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MoveEvent;
import net.minecraft.entity.Entity;

public class MovementUtils
{
    public static MilliTimer strafeTimer;

    public static float getSpeed() {
        return (float)Math.sqrt(KyroClient.mc.thePlayer.motionX * KyroClient.mc.thePlayer.motionX + KyroClient.mc.thePlayer.motionZ * KyroClient.mc.thePlayer.motionZ);
    }

    public static float getSpeed(final double x, final double z) {
        return (float)Math.sqrt(x * x + z * z);
    }

    public static boolean isMoving() {
        return KyroClient.mc.thePlayer.moveForward != 0.0f || KyroClient.mc.thePlayer.moveStrafing != 0.0f;
    }

    public static boolean hasMotion() {
        return KyroClient.mc.thePlayer.motionX != 0.0 && KyroClient.mc.thePlayer.motionZ != 0.0 && KyroClient.mc.thePlayer.motionY != 0.0;
    }

    public static boolean isOnGround(final double height) {
        return !KyroClient.mc.theWorld.getCollidingBoundingBoxes((Entity)KyroClient.mc.thePlayer, KyroClient.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static void strafe(final float speed, final float yaw) {
        if (!isMoving() || !MovementUtils.strafeTimer.hasTimePassed(150L)) {
            return;
        }
        KyroClient.mc.thePlayer.motionX = -Math.sin(Math.toRadians(yaw)) * speed;
        KyroClient.mc.thePlayer.motionZ = Math.cos(Math.toRadians(yaw)) * speed;
        MovementUtils.strafeTimer.reset();
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(KyroClient.mc.thePlayer.rotationYaw);
        KyroClient.mc.thePlayer.setPosition(KyroClient.mc.thePlayer.posX + -Math.sin(yaw) * length, KyroClient.mc.thePlayer.posY, KyroClient.mc.thePlayer.posZ + Math.cos(yaw) * length);
    }

    static {
        MovementUtils.strafeTimer = new MilliTimer();
    }
}
