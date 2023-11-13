package me.kyroclient;

import com.mojang.authlib.Agent;
import me.kyroclient.mixins.ClientMixinService;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.service.MixinService;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class KyroClientLoader {
    public static void start(Instrumentation inst)
    {
        System.out.println("Initializing KyroClient Loader!");

        String path = AgentLoader.dir;

        if (path == null)
        {
            System.out.println("Failed to download Kyro Client");
            AgentLoader.exception.printStackTrace();
            return;
        }

        MixinBootstrap.init();

        if (!isLoaded("me.kyroclient.KyroClient"))
        {
            System.out.println("Kyroclient jar not loaded!");
        }

        Mixins.addConfiguration("mixins.kyroclient.json");

        System.out.println("Worked!");
    }

    public static boolean isLoaded(String className)
    {
        try
        {
            Class.forName(className);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
