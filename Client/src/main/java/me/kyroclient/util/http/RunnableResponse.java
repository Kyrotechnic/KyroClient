package me.kyroclient.util.http;

public class RunnableResponse implements Runnable {
    public void set(Response response)
    {
        this.response = response;
    }
    public RunnableResponse renew(Runnable runnable)
    {
        this.runnable = runnable;

        return this;
    }
    public Response response;
    public Runnable runnable;

    @Override
    public void run() {
        runnable.run();
    }
}
