package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;

public class Velocity extends Module {
    public NumberSetting vModifier;
    public NumberSetting hModifier;
    public Velocity()
    {
        super("Velocity", Category.PLAYER);

        this.vModifier = new NumberSetting("Vertical", 0.0, -2.0, 2.0, 0.05);
        this.hModifier = new NumberSetting("Horizontal", 0.0, -2.0, 2.0, 0.05);

        addSettings(vModifier, hModifier);
    }

    @Override
    public void assign()
    {
        KyroClient.velocity = this;
    }
}
