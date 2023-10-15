package me.kyroclient.util;

import me.kyroclient.KyroClient;
import me.kyroclient.mixins.MinecraftAccessor;
import net.minecraft.util.Timer;

public class TimerUtil
{
    public static void setSpeed(final float speed) {
        getTimer().timerSpeed = speed;
    }

    public static void resetSpeed() {
        setSpeed(1.0f);
    }

    public static Timer getTimer() {
        return ((MinecraftAccessor) KyroClient.mc).getTimer();
    }
}
