package me.kyroclient.modules.client;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MemoryFix extends Module {
    public NumberSetting delay = new NumberSetting("Delay (ticks)", 4, 1, 20, 1);
    public MemoryFix()
    {
        super("Memory Fix", Category.CLIENT);

        addSettings(
                delay
        );
    }

    @SubscribeEvent
    public void memoryRelease(TickEvent.ClientTickEvent event)
    {
        if (isToggled() && KyroClient.ticks % delay.getValue() == 0)
            System.gc();
    }

}