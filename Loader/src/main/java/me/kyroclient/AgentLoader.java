package me.kyroclient;

import lombok.SneakyThrows;

import javax.xml.transform.Transformer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class AgentLoader {
    public static final String VERSION_LINK = "https://github.com/Kyrotechnic/KyroClient/raw/main/update/Latest.txt";
    public static final String VERSION_DOWNLOAD = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/KyroClient.jar";
    public static final String MOD_LOCATION = System.getenv("APPDATA") + "\\.minecraft\\test.jar";
    public static final String LOCALE_VERSION = System.getenv("APPDATA") + "\\.minecraft\\config\\KyroClient\\Version.txt";
    public static boolean needUpdate = false;

    public static void premain(String agentArgs, Instrumentation instrumentation) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AgentLoader.downloadUpdate();

        JarFile file = new JarFile(new File(MOD_LOCATION));

        instrumentation.appendToBootstrapClassLoaderSearch(file);
        instrumentation.appendToSystemClassLoaderSearch(file);

        System.out.println("Added to class loader!");

        String test = "net/minecraftforge/fml/relauncher/CoreModManager";

        instrumentation.addTransformer(new SafeTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, byte[] originalClass) {
                if (className.contains("CoreModManager"))
                    loadMod();

                return originalClass;
            }
        });
    }

    public static boolean loaded = false;

    public static void loadMod()
    {
        if (loaded) return;
        try {
            Class clasy = Class.forName("net.minecraft.launchwrapper.Launch");
            Map<String, Object> map = (Map<String, Object>) clasy.getDeclaredField("blackboard").get(null);
            List<String> tweaks = (List<String>) map.get("TweakClasses");
            tweaks.add("me.kyroclient.tweaker.Tweaker");

            System.out.println("YAY!");

            Class clasz = Class.forName("net.minecraftforge.fml.relauncher.ModListHelper");

            Field field = clasz.getDeclaredField("additionalMods");
            Map<String, File> mods = (Map<String, File>) field.get(null);
            mods.put("kyroclient", new File(MOD_LOCATION));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loaded = true;
        System.out.println("Loaded mod!");
    }

    public static void downloadUpdate() {
        try {
            URL downloadUrl = new URL(VERSION_DOWNLOAD);
            File file = new File(MOD_LOCATION);
            try (InputStream in = downloadUrl.openStream();){
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            File file2 = new File(LOCALE_VERSION);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
