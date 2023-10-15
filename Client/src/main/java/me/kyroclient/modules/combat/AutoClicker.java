package me.kyroclient.modules.combat;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MathUtil;
import me.kyroclient.util.MilliTimer;
import me.kyroclient.util.PlayerUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoClicker extends Module {
    public static final NumberSetting maxCps;
    public static final NumberSetting minCps;
    public static final ModeSetting mode;
    private MilliTimer timer;
    private double nextDelay;

    public AutoClicker() {
        super("AutoClicker", Category.COMBAT);
        this.timer = new MilliTimer();
        this.nextDelay = 10.0;
        this.addSettings(AutoClicker.minCps, AutoClicker.maxCps, AutoClicker.mode);
    }

    @SubscribeEvent
    public void onTick(final RenderWorldLastEvent event) {
        if (this.isToggled() && KyroClient.mc.thePlayer != null && this.isPressed() && !KyroClient.mc.thePlayer.isUsingItem() && KyroClient.mc.currentScreen == null && this.timer.hasTimePassed((long)(1000.0 / this.nextDelay))) {
            this.timer.reset();
            this.nextDelay = MathUtil.getRandomInRange(AutoClicker.maxCps.getValue(), AutoClicker.minCps.getValue());
            PlayerUtil.click();
        }
    }

    @Override
    public boolean isPressed() {
        final String selected = AutoClicker.mode.getSelected();
        switch (selected) {
            case "Key held": {
                return super.isPressed();
            }
            case "Toggle": {
                return this.isToggled();
            }
            default: {
                return KyroClient.mc.gameSettings.keyBindAttack.isKeyDown();
            }
        }
    }

    static {
        maxCps = new NumberSetting("Max CPS", 12.0, 1.0, 20.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                AutoClicker.minCps.setMax(AutoClicker.maxCps.getValue());
                if (AutoClicker.minCps.getValue() > AutoClicker.minCps.getMax()) {
                    AutoClicker.minCps.setValue(AutoClicker.minCps.getMin());
                }
            }
        };
        minCps = new NumberSetting("Min CPS", 10.0, 1.0, 20.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                AutoClicker.maxCps.setMin(AutoClicker.minCps.getValue());
                if (AutoClicker.maxCps.getValue() < AutoClicker.maxCps.getMin()) {
                    AutoClicker.maxCps.setValue(AutoClicker.maxCps.getMin());
                }
            }
        };
        mode = new ModeSetting("Mode", "Attack held", new String[] { "Key held", "Toggle", "Attack held" });
    }
}
