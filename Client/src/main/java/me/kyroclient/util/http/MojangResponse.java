package me.kyroclient.util.http;

import com.google.gson.JsonObject;

import java.util.UUID;

public class MojangResponse extends Response {
    public String username;
    public UUID uuid;
    public String errorMessage = null;
    public MojangResponse() {
        super(true);
    }

    @Override
    public MojangResponse parse(JsonObject result) {
        if (result.has("errorMessage"))
        {
            errorMessage = result.get("errorMessage").getAsString();
            return this;
        }
        uuid = java.util.UUID.fromString(
                result.get("id").getAsString()
                        .replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                        )
        );
        username = result.get("name").getAsString();

        return this;
    }
}
