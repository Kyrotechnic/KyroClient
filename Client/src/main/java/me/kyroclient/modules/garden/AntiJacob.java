package me.kyroclient.modules.garden;


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
    public void scoreboard(TickEvent.ClientTickEvent e)
    {
        if (!isToggled()) return;

        if (ScoreboardUtils.contains("�eJacob's Contest") && (KyroClient.macro.isToggled() || KyroClient.cropNuker.isToggled()))
        {
            KyroClient.macro.setToggled(false);
            KyroClient.cropNuker.setToggled(false);

            disabled = true;
        }
        else if (disabled && !ScoreboardUtils.contains("�eJacob's Contest"))
        {
            disabled = false;

            KyroClient.macro.setToggled(true);
            KyroClient.cropNuker.setToggled(true);
        }
    }
}
