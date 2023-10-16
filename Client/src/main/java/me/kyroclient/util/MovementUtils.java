package me.kyroclient.util;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MoveEvent;
import me.kyroclient.modules.combat.Aura;
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

    public static void strafe() {
        strafe(getSpeed());
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

    public static void strafe(final double speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        KyroClient.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        KyroClient.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
        MovementUtils.strafeTimer.reset();
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

    public static double getDirection() {
        return Math.toRadians(getYaw());
    }

    public static void setMotion(final MoveEvent em, final double speed) {
        double forward = KyroClient.mc.thePlayer.movementInput.moveForward;
        double strafe = KyroClient.mc.thePlayer.movementInput.moveStrafe;
        float yaw = ((Aura.target != null && KyroClient.aura.movementFix.isEnabled())) ? RotationUtils.getRotations(Aura.target).getYaw() : KyroClient.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            KyroClient.mc.thePlayer.motionX = 0.0;
            KyroClient.mc.thePlayer.motionZ = 0.0;
            if (em != null) {
                em.setX(0.0);
                em.setZ(0.0);
            }
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            KyroClient.mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            KyroClient.mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
            if (em != null) {
                em.setX(KyroClient.mc.thePlayer.motionX);
                em.setZ(KyroClient.mc.thePlayer.motionZ);
            }
        }
    }

    public static float getYaw() {
        float yaw = ((Aura.target != null && KyroClient.aura.movementFix.isEnabled())) ? RotationUtils.getRotations(Aura.target).getYaw() : KyroClient.mc.thePlayer.rotationYaw;
        if (KyroClient.mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (KyroClient.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (KyroClient.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (KyroClient.mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (KyroClient.mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw;
    }

    static {
        MovementUtils.strafeTimer = new MilliTimer();
    }
}
