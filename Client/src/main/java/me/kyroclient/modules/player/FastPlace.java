package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;

public class FastPlace extends Module {
    public NumberSetting placeDelay;
    public FastPlace()
    {
        super("Fast Place", Category.PLAYER);

        this.addSettings(this.placeDelay = new NumberSetting("Place delay", 2.0, 0.0, 4.0, 1.0));
    }

    @Override
    public void assign()
    {
        KyroClient.fastPlace = this;
    }
}
