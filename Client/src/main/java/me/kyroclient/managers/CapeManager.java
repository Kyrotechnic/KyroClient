package me.kyroclient.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.kyroclient.capes.CapeBearer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CapeManager {
    public final String CAPE_URL = "https://raw.githubusercontent.com/Kyrotechnic/KyroClient/main/assets/Capes.json";
    public HashMap<String, CapeBearer> capeList = new HashMap<>();

    public void init() throws Exception {
        JsonArray objectArr = new JsonParser().parse(new JsonReader(new InputStreamReader(new URL(CAPE_URL).openConnection().getInputStream()))).getAsJsonArray();

        for (JsonElement element : objectArr)
        {
            JsonObject object = element.getAsJsonObject();

            String username = object.get("username").getAsString();
            String capeUrl = object.get("cape").getAsString();

            JsonArray accounts = object.get("accounts").getAsJsonArray();
            String[] accs = new String[accounts.size()];

            for (int i = 0; i < accounts.size(); i++)
            {
                accs[i] = accounts.get(i).getAsString();
            }

            CapeBearer bearer = new CapeBearer(username, capeUrl);

            for (String acc : accs)
            {
                capeList.put(acc, bearer);
            }
        }
    }


}
