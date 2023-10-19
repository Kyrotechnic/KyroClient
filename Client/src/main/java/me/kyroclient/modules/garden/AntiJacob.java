package me.kyroclient.modules.garden;


import at.hannibal2.skyhanni.events.FarmingContestEvent;
import me.kyroclient.KyroClient;
import me.kyroclient.events.ScoreboardRenderEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.util.ScoreboardUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiJacob extends Module {
    public AntiJacob()
    {
        super("Anti Jacob", Category.GARDEN);
    }

    public boolean disabled = false;
    @Override
    public void onEnable()
    {
        disabled = false;
    }

    @Override
    public void onDisable()
    {
        disabled = false;
    }

    @SubscribeEvent
    public void onSkyHanni(FarmingContestEvent event)
    {
        if (!isToggled()) return;

        switch (event.getPhase())
        {
            case START:
                disabled = true;
                update(false);
            case STOP:
                if (!disabled) return;
                update(true);
                disabled = false;
        }
    }

    public void update(boolean state)
    {
        KyroClient.cropNuker.setToggled(state);
        KyroClient.macro.setToggled(state);
    }
}
