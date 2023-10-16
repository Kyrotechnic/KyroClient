package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;

public class Giants extends Module
{
    public NumberSetting scale;
    public BooleanSetting mobs;
    public BooleanSetting players;
    public BooleanSetting armorStands;

    public Giants() {
        super("Giants", Category.RENDER);
        this.scale = new NumberSetting("Scale", 1.0, 0.1, 5.0, 0.1);
        this.mobs = new BooleanSetting("Mobs", true);
        this.players = new BooleanSetting("Players", true);
        this.armorStands = new BooleanSetting("ArmorStands", false);
        this.addSettings(this.scale, this.players, this.mobs, this.armorStands);
    }

    @Override
    public void assign()
    {
        KyroClient.giants = this;
    }
}
