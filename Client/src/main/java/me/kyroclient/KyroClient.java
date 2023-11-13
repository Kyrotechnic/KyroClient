package me.kyroclient;


import lombok.SneakyThrows;
import me.kyroclient.forge.ForgeRegister;
import me.kyroclient.forge.ForgeSpoofer;
import me.kyroclient.managers.*;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.client.Tickless;
import me.kyroclient.modules.combat.*;
import me.kyroclient.modules.garden.CropNuker;
import me.kyroclient.modules.garden.FarmingMacro;
import me.kyroclient.modules.mining.MiningQOL;
import me.kyroclient.modules.misc.Delays;
import me.kyroclient.modules.misc.GuiMove;
import me.kyroclient.modules.misc.Modless;
import me.kyroclient.modules.misc.NoSlow;
import me.kyroclient.modules.player.FastPlace;
import me.kyroclient.modules.player.NickHider;
import me.kyroclient.modules.player.Speed;
import me.kyroclient.modules.player.Velocity;
import me.kyroclient.modules.render.*;
import me.kyroclient.notifications.Notification;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.BlurUtils;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mixins;

import java.awt.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class KyroClient {
    //Vars
    public static final String MOD_ID = "dankers";
    public static String VERSION = "0.2-b3";
    public static List<String> changelog;
    public static ModuleManager moduleManager;
    public static NotificationManager notificationManager;
    public static ConfigManager configManager;
    public static ThemeManager themeManager;
    public static Minecraft mc;
    public static boolean isDev = true;
    public static Color iconColor = new Color(237, 107, 0);
    public static long gameStarted;
    public static boolean banned = false;

    //Module Dependencies
    public static Gui clickGui;
    public static NickHider nickHider;
    public static CropNuker cropNuker;
    public static Velocity velocity;
    public static FastPlace fastPlace;
    public static Modless modless;
    public static Speed speed;
    public static AntiBot antiBot;
    public static Interfaces interfaces;
    public static Delays delays;
    public static Hitboxes hitBoxes;
    public static NoSlow noSlow;
    public static FreeCam freeCam;
    public static Giants giants;
    public static Animations animations;
    public static Aura aura;
    public static AutoBlock autoBlock;
    public static PopupAnimation popupAnimation;
    public static GuiMove guiMove;
    public static Camera camera;
    public static Reach reach;
    public static InventoryDisplay inventoryDisplay;
    public static LoreDisplay loreDisplay;
    public static FarmingMacro macro;
    public static MiningQOL miningQol;
    public static Tickless tickless;


    //Methods

    /*
    Time to finally make it spoof being any random mod on boot!
     */
    public static List<ForgeRegister> registerEvents()
    {
        /*for (Module module : moduleManager.getModules())
        {
            MinecraftForge.EVENT_BUS.register(module);
        }*/

        ForgeSpoofer.update();
        List<ForgeRegister> registers = new ArrayList<>();

        for (Module module : moduleManager.getModules())
        {
            List<ForgeRegister> register = ForgeSpoofer.register(module, true);
            if (register.isEmpty()) continue;
            registers.addAll(register);
        }

        registers.add(ForgeSpoofer.register(notificationManager = new NotificationManager(), true).get(0));
        registers.add(ForgeSpoofer.register(new BlurUtils(), true).get(0));

        Fonts.bootstrap();

        return registers;
    }

    public static void mixin()
    {
        Mixins.addConfiguration("mixins.kyroclient.json");
    }

    public static void init()
    {
        mc = Minecraft.getMinecraft();

        moduleManager = new ModuleManager("me.kyroclient.modules");
        moduleManager.initReflection();

        configManager = new ConfigManager();
        themeManager = new ThemeManager();

        CommandManager.init();

        gameStarted = System.currentTimeMillis();

        new Thread(KyroClient::threadTask).start();
    }

    @SneakyThrows
    public static void threadTask()
    {
        URL url2 = new URL("https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/Changelog.txt");

        List<String> changelog = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
        String b = null;
        while ((b = reader.readLine()) != null)
        {
            changelog.add(b);
        }

        KyroClient.VERSION = changelog.get(0);

        KyroClient.changelog = changelog.stream().filter(c -> !(c.equals(KyroClient.VERSION))).collect(Collectors.toList());
    }

    public static void handleKey(int key)
    {
        for (Module module : moduleManager.getModules())
        {
            if (module.getKeycode() == key)
            {
                module.toggle();
                if (!clickGui.disableNotifs.isEnabled())
                    notificationManager.showNotification(module.getName() + " has been " + (module.isToggled() ? "Enabled" : "Disabled"), 2000, Notification.NotificationType.INFO);
            }
        }
    }
}
