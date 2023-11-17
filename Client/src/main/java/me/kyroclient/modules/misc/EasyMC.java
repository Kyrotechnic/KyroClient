package me.kyroclient.modules.misc;

import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;

public class EasyMC extends Module {
    public BooleanSetting save = new BooleanSetting("Save Previous", true);
    public EasyMC()
    {
        super("Easy Alt", Category.MISC);

        addSettings(
                save
        );
    }
}
