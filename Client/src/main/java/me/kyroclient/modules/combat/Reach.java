package me.kyroclient.modules.combat;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;

public class Reach extends Module
{
    public NumberSetting reach;
    public NumberSetting blockReach;

    public Reach() {
        super("Reach", 0, Category.COMBAT);
        this.reach = new NumberSetting("Range", 3.0, 2.0, 4.5, 0.1);
        this.blockReach = new NumberSetting("Block Range", 4.5, 2.0, 6.0, 0.01);
        this.addSettings(this.reach, this.blockReach);
    }

    @Override
    public void assign()
    {
        KyroClient.reach = this;
    }
}
