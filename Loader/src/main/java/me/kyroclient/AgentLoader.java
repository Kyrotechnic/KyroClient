package me.kyroclient;

import me.kyroclient.transformer.SafeTransformer;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.ProtectionDomain;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AgentLoader {
    public static void premain(String agentArgs, Instrumentation inst)
    {
        dir = downloadUpdate();

        JarFile jarFile;

        try {
            jarFile = new JarFile(new File(dir));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        inst.appendToSystemClassLoaderSearch(jarFile);
        inst.appendToBootstrapClassLoaderSearch(jarFile);

        File file = new File(dir);
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(loader, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }


        inst.addTransformer(new SafeTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, byte[] originalClass) {
                if (className.startsWith("net/minecraft/client/"))
                {
                    inst.removeTransformer(this);

                    try {
                        loader.loadClass("me.kyroclient.KyroClientLoader")
                                .getDeclaredMethod("start", Instrumentation.class)
                                .invoke(null, inst);
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                return null;
            }
        });
    }

    public static final String VERSION_LINK = "https://github.com/Kyrotechnic/KyroClient/raw/main/update/Latest.txt";
    public static final String VERSION_DOWNLOAD = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/KyroClient.jar";
    public static final String MOD_LOCATION = System.getenv("APPDATA") + "\\.minecraft\\mods\\KyroClient.jar";
    public static final String LOCALE_VERSION = System.getenv("APPDATA") + "\\.minecraft\\config\\KyroClient\\test.jar";
    public static boolean needUpdate = false;
    public static String dir = LOCALE_VERSION;
    public static Exception exception = null;

    public static String downloadUpdate()
    {
        try
        {
            dir = LOCALE_VERSION;

            URL downloadUrl = new URL(VERSION_DOWNLOAD);
            File file = new File(dir);

            try (InputStream in = downloadUrl.openStream())
            {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (Exception e)
        {
            exception =  e;
            e.printStackTrace();
            dir = null;
        }

        return dir = LOCALE_VERSION;
    }
}
