package me.kyroclient.modules;

import me.kyroclient.KyroClient;

public class ClientSettings extends Module {
    public ClientSettings() {
        super("Client Settings", 0, Category.MISC);
    }

    @Override
    public void assign()
    {
        KyroClient.clientSettings = this;
    }
}
