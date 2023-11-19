package me.kyroclient;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.MixinTweaker;
import org.spongepowered.asm.mixin.Mixins;
import sun.misc.Launcher;

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
    public static final String MOD_LOCATION = System.getenv("APPDATA") + "\\.minecraft\\config\\KyroClient\\test.jar";
    public static final String LOCALE_VERSION = System.getenv("APPDATA") + "\\.minecraft\\config\\KyroClient\\Version.txt";
    public static boolean downloaded = false;
    public static void downloadUpdate() {
        try {
            URL downloadUrl = new URL(VERSION_DOWNLOAD);
            File file = new File(MOD_LOCATION);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (InputStream in = downloadUrl.openStream();){
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            downloaded = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
