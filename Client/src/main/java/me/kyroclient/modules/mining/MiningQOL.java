package me.kyroclient.modules.mining;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;

public class MiningQOL extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Skyblock", "None");
    public NumberSetting threshold = new NumberSetting("Threshold", 0.7, 0.7, 1.0, 0.01, aBoolean -> !mode.is("Vanilla"));
    public NumberSetting ticks = new NumberSetting("Ticks", 20, 1, 100, 1.0, aBoolean -> !mode.is("Skyblock"));
    public BooleanSetting modifyHitDelay = new BooleanSetting("Modify Hit Delay", false);
    public NumberSetting newDelay = new NumberSetting("New Delay", 0, 0, 5, 1, aBoolean -> !modifyHitDelay.isEnabled());
    public BooleanSetting noReset = new BooleanSetting("No Reset", false);
    public MiningQOL()
    {
        super("Mining QOL", Category.MINING);

        addSettings(
                mode,
                threshold,
                ticks,
                modifyHitDelay,
                newDelay,
                noReset
        );
    }

    @Override
    public void assign()
    {
        KyroClient.miningQol = this;
    }

    public boolean preventReset()
    {
        return isToggled() && noReset.isEnabled();
    }

    public boolean shouldRemoveHitDelay(int delay)
    {
        return this.isToggled() && modifyHitDelay.isEnabled() && delay <= (5.0 - newDelay.getValue());
    }

    public boolean shouldPreBreakBlock(float ticks, float damage)
    {
        return isToggled() && mode.is("Skyblock") && damage < 1f && ticks >= this.ticks.getValue();
    }

    public boolean shouldTweakVanilla()
    {
        return isToggled() && mode.is("Vanilla");
    }
}
