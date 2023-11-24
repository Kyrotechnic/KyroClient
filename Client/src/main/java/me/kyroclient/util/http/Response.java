package me.kyroclient.util.http;

import com.google.gson.JsonObject;

public abstract class Response {
    public boolean successful = false;
    public RunnableResponse runnable;
    public Response(boolean success)
    {
        successful = success;
    }

    public Response then(RunnableResponse runnable)
    {
        this.runnable = runnable;

        return this;
    }

    public void finish()
    {
        runnable.set(this);

        runnable.run();
    }

    public abstract Response parse(JsonObject object);
}
