package me.kyroclient.modules.misc;

import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;

public class Delays extends Module {
    public NumberSetting hitDelay = new NumberSetting("Hit Delay", 0.0, 0.0, 10, 1);
    public NumberSetting jumpDelay = new NumberSetting("Jump Delay", 0, 0, 10, 1);
    public Delays()
    {
        super("Delays", Category.MISC);

        addSettings(
                hitDelay,
                jumpDelay
        );
    }
}
