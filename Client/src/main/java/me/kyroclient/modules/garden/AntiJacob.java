package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;

public class AntiJacob extends Module {
    public BooleanSetting disableNuker = new BooleanSetting("Disable Nuker", true);
    public BooleanSetting disableMacro = new BooleanSetting("Disable Macro", true);
    public BooleanSetting autoReenable = new BooleanSetting("Auto Re-enable", true);
    public static AntiJacob instance;
    public AntiJacob()
    {
        super("Anti Jacob", Module.Category.GARDEN);

        instance = this;

        addSettings(
                disableNuker,
                disableMacro,
                autoReenable
        );
    }

    public void disable()
    {
        if (!isToggled()) return;

        if (disableMacro.isEnabled())
            KyroClient.macro.setToggled(false);

        if (disableNuker.isEnabled())
            KyroClient.cropNuker.setToggled(false);
    }

    public void reenable()
    {
        if (!isToggled()) return;
        if (!autoReenable.isEnabled()) return;

        if (disableMacro.isEnabled())
            KyroClient.macro.setToggled(true);

        if (disableNuker.isEnabled())
            KyroClient.cropNuker.setToggled(true);
    }
}
