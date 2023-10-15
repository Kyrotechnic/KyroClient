package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;

public class Modless extends Module {
    public Modless()
    {
        super("Modless", Category.MISC);
    }

    @Override
    public void assign()
    {
        KyroClient.modless = this;
    }
}
