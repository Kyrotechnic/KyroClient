package me.kyroclient.util;

import lombok.SneakyThrows;
import me.kyroclient.KyroClient;

public class Process {
    public Thread thread;
    public Process(Runnable runnable)
    {
        thread = new Thread(runnable);
        thread.start();
    }

    public void run()
    {
        thread.start();
    }

    @SneakyThrows
    public void sleep(long l)
    {
        Thread.sleep(l);
    }
}
