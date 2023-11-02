package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.StepEvent;
import me.kyroclient.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Step extends Module {
    public Step()
    {
        super("Step", Category.PLAYER);
    }

    @SubscribeEvent
    public void onStep(StepEvent event)
    {
        if (isToggled() && !KyroClient.mc.thePlayer.movementInput.jump && !KyroClient.mc.thePlayer.isInWater() && !KyroClient.mc.thePlayer.isInLava())
        {
            event.setHeight(1);
        }
    }
}
