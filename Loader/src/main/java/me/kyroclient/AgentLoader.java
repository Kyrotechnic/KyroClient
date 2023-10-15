package me.kyroclient;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class AgentLoader {
    public static final String VERSION_LINK = "https://github.com/Kyrotechnic/KyroClient/raw/main/update/Latest.txt";
    public static final String VERSION_DOWNLOAD = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/KyroClient.jar";
    public static final String MOD_LOCATION = System.getenv("APPDATA") + "\\.minecraft\\mods\\KyroClient.jar";
    public static final String LOCALE_VERSION = System.getenv("APPDATA") + "\\.minecraft\\config\\KyroClient\\Version.txt";
    public static boolean needUpdate = false;
    public static void premain(String agentArgs, Instrumentation instrumentation)
    {
        downloadUpdate();
    }

    public static void downloadUpdate()
    {
        try
        {
            URL downloadUrl = new URL(VERSION_DOWNLOAD);
            File file = new File(MOD_LOCATION);
            try (InputStream in = downloadUrl.openStream())
            {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            File versionFile = new File(LOCALE_VERSION);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
