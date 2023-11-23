package me.kyroclient.modules.client;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;

public class Capes extends Module {
    public Capes()
    {
        super("Capes", Category.CLIENT);
    }

    @Override
    public void assign()
    {
        KyroClient.capes = this;
    }
}
