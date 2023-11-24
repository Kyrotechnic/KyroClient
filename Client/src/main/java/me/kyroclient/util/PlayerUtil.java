package me.kyroclient.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.kyroclient.KyroClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.ScoreObjective;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerUtil {
    public static boolean lastGround = false;
    public static void swingItem()
    {
        KyroClient.mc.thePlayer.swingItem();
    }
    public static Method clickMouse;
    public static boolean onHypixel()
    {
        return KyroClient.mc.getCurrentServerData().serverIP.endsWith("hypixel.net");
    }
    public static void click()
    {
        if (clickMouse != null)
        {
            try {
                clickMouse.invoke(KyroClient.mc);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return;
        }
        try {
            try {
                clickMouse = Minecraft.class.getDeclaredMethod("func_147116_af");
            }
            catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
            clickMouse.setAccessible(true);
            clickMouse.invoke(Minecraft.getMinecraft(), new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isOnSkyBlock() {
        try {
            final ScoreObjective titleObjective = KyroClient.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (KyroClient.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(0) != null) {
                return ChatFormatting.stripFormatting(KyroClient.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(0).getDisplayName()).contains("SKYBLOCK");
            }
            return ChatFormatting.stripFormatting(KyroClient.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).contains("SKYBLOCK");
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean isNPC(final Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
        return ChatFormatting.stripFormatting(entity.getDisplayName().getUnformattedText()).startsWith("[NPC]") || (entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0f && entityLivingBase.getMaxHealth() == 20.0f);
    }

    public static void swapSlot(int i)
    {
        KyroClient.mc.thePlayer.inventory.currentItem = i;
    }
}
