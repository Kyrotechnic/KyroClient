package org.example;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Main {
    public static List<String> NATIVES = List.of("avutil-ttv-51.dll", "jinput-dx8.dll", "jinput-dx8_64.dll", "jinput-raw.dll", "jinput-raw_64.dll", "jinput-wintab.dll", "libmfxsw64.dll", "libmp3lame-ttv.dll", "lwjgl.dll", "lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll", "swresample-ttv-0.dll", "twitchsdk.dll");
    public static final String FORGE_INSTALL_BASE = 
    public static final String APPDATA = System.getenv("APPDATA");
    public static void main(String[] args) {
        System.out.println("Installing KyroClient forge edition");

        File forgeFile = new File(APPDATA + "\\versions\\k_1.8.9-forge\\");

        if (!forgeFile.exists())
        {
            forgeFile.mkdirs();
            installForgeFiles();
        }


    }

    public static void installForgeFiles()
    {

    }

    public static boolean install(String url, File install)
    {
        try
        {
            URL newurl = new URL(url);

            if (!install.getParentFile().exists())
                install.getParentFile().mkdirs();

            try (InputStream stream = newurl.openStream())
            {
                Files.copy(stream, install.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}