package me.kyroclient.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.kyroclient.util.http.MojangResponse;
import me.kyroclient.util.http.RunnableResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class PlayerAPI {
    public static final String NAME_UUID_API = "https://api.mojang.com/users/profiles/minecraft/";
    public static final String UUID_NAME_API = "https://api.mojang.com/user/profile/";

    public static MojangResponse getUUIDFromName(String username, RunnableResponse runnable)
    {
        MojangResponse response = new MojangResponse();

        response.then(runnable);

        new Process(() -> {
           JsonObject object = getJsonObjectFromURL(NAME_UUID_API + username);

           response.parse(object);

           response.finish();
        });

        return response;
    }

    public static MojangResponse getNameFromUUID(UUID uuid, RunnableResponse runnable)
    {
        MojangResponse response = new MojangResponse();

        response.then(runnable);

        new Process(() -> {
            JsonObject object = getJsonObjectFromURL(UUID_NAME_API + uuid.toString());

            response.parse(object);

            response.finish();
        });

        return response;
    }

    @SneakyThrows
    private static JsonObject getJsonObjectFromURL(String url)
    {
        return new JsonParser().parse(new InputStreamReader(new URL(url).openConnection().getInputStream())).getAsJsonObject();
    }
}
