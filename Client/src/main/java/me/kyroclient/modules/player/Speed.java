package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;

public class Speed extends Module {
    public Speed()
    {
        super("Speed", Category.PLAYER);
    }

    @Override
    public void assign()
    {
        KyroClient.speed = this;
    }
}
