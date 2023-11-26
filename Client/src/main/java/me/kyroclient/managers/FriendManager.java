package me.kyroclient.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.kyroclient.KyroClient;
import me.kyroclient.util.PlayerAPI;
import me.kyroclient.util.Process;
import me.kyroclient.util.http.MojangResponse;
import me.kyroclient.util.http.RunnableResponse;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendManager {
    public File friendsFile;
    public HashMap<UUID, String> playerUUID = new HashMap<>();
    @SneakyThrows
    public void init()
    {
        friendsFile = new File(KyroClient.mc.mcDataDir + "/config/KyroClient/friends.cfg");

        if (!friendsFile.exists())
        {
            Files.createFile(friendsFile.toPath());
        }

        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean has(UUID player)
    {
        if (playerUUID.containsKey(player))
            return true;
        return false;
    }

    public void save() {
        try {
            Files.write(friendsFile.toPath(), getStringFormatted());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getStringFormatted()
    {
        return playerUUID.keySet().stream().map(c -> c.toString()).collect(Collectors.toList());
    }

    public void load() throws IOException {
        List<String> stringList = Files.readAllLines(friendsFile.toPath());

        new Process(() -> {
           for (String uuid : stringList)
           {
               UUID playerUuid = UUID.fromString(uuid);

               RunnableResponse response = new RunnableResponse();

               PlayerAPI.getNameFromUUID(playerUuid, response.renew(() -> {
                    MojangResponse response1 = (MojangResponse) response.response;

                    playerUUID.put(response1.uuid, response1.username);

                    System.out.println("Loaded user " + response1.username + ":" + response1.uuid);
               }));

               try {
                   Thread.sleep(200);
               } catch (Exception e) {

               }
           }
        });
    }

    public void add(String name)
    {
        RunnableResponse response1 = new RunnableResponse();

        PlayerAPI.getUUIDFromName(name, response1.renew(() -> {
            MojangResponse response = (MojangResponse) response1.response;

            if (response.errorMessage != null)
            {
                System.out.println("Error: " + response.errorMessage);
                KyroClient.sendMessage("§6[KyroClient] §f" + response.errorMessage);
                return;
            }

            if (playerUUID.containsKey(response.uuid))
            {
                System.out.println("Player already added");
                KyroClient.sendMessage("§6[KyroClient] §a" + response.username + " is already your friend!");
                return;
            }

            playerUUID.put(response.uuid, response.username);

            KyroClient.sendMessage("§6[KyroClient] §fAdded §a" + response.username + " §fas a friend!");
        }));
    }

    public void remove(String name)
    {
        RunnableResponse response1 = new RunnableResponse();

        PlayerAPI.getUUIDFromName(name, response1.renew(() -> {
            MojangResponse response = (MojangResponse) response1.response;

            if (response.errorMessage != null)
            {
                System.out.println("Error: " + response.errorMessage);
                KyroClient.sendMessage("§6[KyroClient] §f" + response.errorMessage);
                return;
            }
            if (playerUUID.containsKey(response.uuid))
                playerUUID.remove(response.uuid);
            else
            {
                KyroClient.sendMessage("§6[KyroClient] §c" + response.username + " §fis not your friend!");
                return;
            }

            KyroClient.sendMessage("§6[KyroClient] §fRemoved &c" + response.username + " §as a friend!");
        }));
    }
}
