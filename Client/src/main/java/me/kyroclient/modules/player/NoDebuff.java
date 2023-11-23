package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;

public class NoDebuff extends Module {
    public NoDebuff()
    {
        super("No Debuff", Category.PLAYER);
    }

    @Override
    public void assign()
    {
        KyroClient.noDebuff = this;
    }
}
