package me.kyroclient.modules.combat;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;

public class Hitboxes extends Module
{
    public BooleanSetting onlyPlayers;
    public NumberSetting expand;

    public Hitboxes() {
        super("Hitboxes", Module.Category.COMBAT);
        this.onlyPlayers = new BooleanSetting("Only players", false);
        this.expand = new NumberSetting("Expand", 0.5, 0.1, 1.0, 0.1);
        this.addSettings(this.expand);
    }

    @Override
    public void assign()
    {
        KyroClient.hitBoxes = this;
    }
}

