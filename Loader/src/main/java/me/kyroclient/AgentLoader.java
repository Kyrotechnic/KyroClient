package me.kyroclient;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AgentLoader {
    public static final String VERSION_LINK = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/Latest.txt";
    public static final String VERSION_DOWNLOAD = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/update/KyroClient.jar";
    public static final String MOD_LOCATION = System.getenv("APPDATA") + "\\.minecraft\\mods\\KyroClient.jar";
    public static final String LOCALE_VERSION = System.getenv("APPDATA") + "\\.minecraft\\config\\KyroClient\\Version.txt";
    public static boolean needUpdate = false;
    public static void premain(String agentArgs, Instrumentation instrumentation)
    {
        Version onlineVersion;
        Version localeVersion;
        try
        {
            onlineVersion = new Version(new BufferedReader(new InputStreamReader(new URL(VERSION_LINK).openStream())).readLine());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        if (!new File(MOD_LOCATION).exists())
        {
            downloadUpdate(onlineVersion);
            return;
        }

        try
        {
            File file = new File(LOCALE_VERSION);
            if (!file.exists())
            {
                needUpdate = true;
                downloadUpdate(onlineVersion);
                return;
            }

            localeVersion = new Version(new BufferedReader(new FileReader(file)).readLine());

            boolean needsUpdate = Version.isUpdated(localeVersion, onlineVersion);

            if (needsUpdate)
            {
                needUpdate = true;
                downloadUpdate(onlineVersion);
            }

            return;
        }
        catch (Exception e)
        {

        }
    }

    public static void downloadUpdate(Version version)
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

            BufferedWriter writer = new BufferedWriter(new FileWriter(versionFile));
            writer.write(version.toString());
            IOUtils.closeQuietly(writer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
