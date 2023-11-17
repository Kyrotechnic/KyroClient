package me.kyroclient.managers;

import me.kyroclient.KyroClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    public File friendsFile;
    public List<String> friends = new ArrayList<>();
    public void init()
    {
        friendsFile = new File(KyroClient.mc.mcDataDir + "/config/KyroClient/friends.cfg");

        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean has(String name)
    {
        for (String str : friends)
        {
            if (str.toLowerCase().contains(name.toLowerCase()))
                return true;
        }

        return false;
    }

    public void save() {
        try {
            Files.write(friendsFile.toPath(), friends);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() throws IOException {
        friends = Files.readAllLines(friendsFile.toPath());
    }

    public void add(String name)
    {
        friends.add(name);
        save();
    }

    public void remove(String name)
    {
        friends.remove(name);
        save();
    }
}
