package me.kyroclient.tweaker;

import me.kyroclient.AgentLoader;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class Tweaker implements ITweaker {
    public Tweaker()
    {
        AgentLoader.downloadUpdate();
        while (!AgentLoader.downloaded);
    }

    private static Method ADDURL;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile)
    {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        try {
            if (ADDURL == null)
            {
                ADDURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                ADDURL.setAccessible(true);
            }

            ADDURL.invoke(classLoader.getClass().getClassLoader(), new File(AgentLoader.MOD_LOCATION).toURI().toURL());
            classLoader.addURL(new File(AgentLoader.MOD_LOCATION).toURI().toURL());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.kyroclient.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String getLaunchTarget() {
        return null;
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
