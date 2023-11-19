package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Main {
    public static List<String> NATIVES = List.of("avutil-ttv-51.dll", "jinput-dx8.dll", "jinput-dx8_64.dll", "jinput-raw.dll", "jinput-raw_64.dll", "jinput-wintab.dll", "libmfxsw64.dll", "libmp3lame-ttv.dll", "lwjgl.dll", "lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll", "swresample-ttv-0.dll", "twitchsdk.dll");
    public static final String FORGE_INSTALL_BASE = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/assets/";
    public static String forgeFile;
    public static final String APPDATA = System.getenv("APPDATA") + "\\.minecraft";
    public static void main(String[] args) throws IOException {
        System.out.println("Installing KyroClient forge edition");

        File forgeFile = new File(Main.forgeFile = (APPDATA + "\\versions\\k_1.8.9-forge\\"));

        System.out.println("Destination is " + forgeFile);

        if (!forgeFile.exists())
        {
            forgeFile.mkdirs();
            installForgeFiles();
        }

        System.out.println("Installed!");
        Runtime.getRuntime().exec("explorer.exe /select," + forgeFile);
    }

    public static void installForgeFiles()
    {
        install(FORGE_INSTALL_BASE + "k_1.8.9-forge.jar", new File(forgeFile + "k_1.8.9-forge.jar"));
        System.out.println("Installing Forge.jar");
        install(FORGE_INSTALL_BASE + "k_1.8.9-forge.json", new File(forgeFile + "k_1.8.9-forge.json"));
        System.out.println("Installing Forge.json");

        String assets = forgeFile + "natives/";
        new File(assets).mkdirs();

        for (String str : NATIVES)
        {
            install(FORGE_INSTALL_BASE + "natives/" + str, new File(assets + str));
            System.out.println("Installing " + str);
        }

        install("https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/";)
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