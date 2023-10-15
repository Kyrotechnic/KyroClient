package me.kyroclient;


import me.kyroclient.managers.ConfigManager;
import me.kyroclient.managers.ModuleManager;
import me.kyroclient.managers.NotificationManager;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.combat.AntiBot;
import me.kyroclient.modules.garden.CropNuker;
import me.kyroclient.modules.misc.Modless;
import me.kyroclient.modules.player.FastPlace;
import me.kyroclient.modules.player.NickHider;
import me.kyroclient.modules.player.Speed;
import me.kyroclient.modules.player.Velocity;
import me.kyroclient.modules.render.Gui;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.BlurUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.awt.*;

@Mod(modid = "dankers", version = KyroClient.VERSION, clientSideOnly = true)
public class KyroClient {
    //Vars
    public static final String VERSION = "b4";
    public static ModuleManager moduleManager;
    public static NotificationManager notificationManager;
    public static ConfigManager configManager;
    public static Minecraft mc;
    public static boolean isDev = true;
    public static Color iconColor = new Color(237, 107, 0);

    //Module Dependencies
    public static Gui clickGui;
    public static NickHider nickHider;
    public static CropNuker cropNuker;
    public static Velocity velocity;
    public static FastPlace fastPlace;
    public static Modless modless;
    public static Speed speed;
    public static AntiBot antiBot;


    //Methods
    @Mod.EventHandler
    public void startForge(FMLInitializationEvent event)
    {
        for (Module module : moduleManager.getModules())
        {
            MinecraftForge.EVENT_BUS.register(module);
        }

        Fonts.bootstrap();
    }

    public static void init()
    {
        mc = Minecraft.getMinecraft();

        moduleManager = new ModuleManager("me.kyroclient.modules");
        moduleManager.initReflection();

        notificationManager = new NotificationManager();
        configManager = new ConfigManager();

        BlurUtils.registerListener();
    }

    public static void handleKey(int key)
    {
        for (Module module : moduleManager.getModules())
        {
            if (module.getKeycode() == key)
            {
                module.toggle();
            }
        }
    }
}
