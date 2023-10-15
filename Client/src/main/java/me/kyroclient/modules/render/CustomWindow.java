package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.StringSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class CustomWindow extends Module {
    public BooleanSetting showPlayerName;
    public BooleanSetting showTimeSinceLaunch;
    public BooleanSetting showPurseAmount;
    public StringSetting customTitle;
    public CustomWindow()
    {
        super("Custom Window", Category.RENDER);

        addSettings(
                showPlayerName = new BooleanSetting("Show Name", true),
                showTimeSinceLaunch = new BooleanSetting("Played time", true),
                customTitle = new StringSetting("Custom Title", "KyroClient")
        );
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e)
    {
        StringBuilder sb = new StringBuilder();

        if (!customTitle.getValue().equalsIgnoreCase(""))
            sb.append(customTitle.getValue());

        if (showPlayerName.isEnabled())
        {
            if (KyroClient.nickHider.isToggled())
                sb.append(" | " + KyroClient.nickHider.nick.getValue());
            else
                sb.append(" | " + KyroClient.mc.getSession().getUsername());
        }

        if (showTimeSinceLaunch.isEnabled())
            sb.append(" | " + buildString());

    }

    private String buildString()
    {
        long ms = System.currentTimeMillis() - KyroClient.gameStarted;

        long sec = ms/1000;

        int[] timeArr = new int[3];

        while ((sec -= 3600) > 0)
        {
            timeArr[0]++;
        }

        while ((sec -= 60) > 0)
        {
            timeArr[1]++;
        }

        timeArr[2] = (int) sec;

        String hours = timeArr[0] + "";
        String minutes = timeArr[1] > 9 ? timeArr[1] + "" : "0" + timeArr[1];
        String seconds = timeArr[2] > 9 ? timeArr[2] + "" : "0" + timeArr[2];

        return String.format("%s:%s:%s", hours, minutes, seconds);
    }
}
