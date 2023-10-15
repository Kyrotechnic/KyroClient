package me.kyroclient.modules.misc;

import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Timer extends Module {
    public NumberSetting timer;
    public Timer()
    {
        super("Timer", Category.MISC);
        addSettings(
                timer = new NumberSetting("Timer", 1.0, 0.1, 5.0, 0.1)
        );
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && this.isToggled()) {
            TimerUtil.setSpeed((float)timer.getValue());
        }
    }

    @Override
    public void onDisable()
    {
        if (TimerUtil.getTimer() != null)
        {
            TimerUtil.resetSpeed();
        }
    }
}
