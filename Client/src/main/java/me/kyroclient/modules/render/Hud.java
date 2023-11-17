package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;

public class Hud extends Module {
    public BooleanSetting bps;
    public BooleanSetting cps;
    public BooleanSetting speed;
    public BooleanSetting fps;
    public BooleanSetting smoothFps;
    public BooleanSetting modules;
    public BooleanSetting ping;
    public BooleanSetting customHotbar;
    public Hud()
    {
        super("Hud", Category.RENDER);

        addSettings(
                bps = new BooleanSetting("Blocks Per Second", true),
                cps = new BooleanSetting("Clicks Per Second", true),
                fps = new BooleanSetting("Frames Per Second", true),
                smoothFps = new BooleanSetting("Smooth FPS", true, aBoolean -> !fps.isEnabled()),
                ping = new BooleanSetting("Display Ping", true),
                speed = new BooleanSetting("Display Speed", true),
                modules = new BooleanSetting("Module Count", true),
                customHotbar = new BooleanSetting("Custom Hotbar", true)
        );
    }

    @Override
    public void assign()
    {
        KyroClient.hud = this;
    }

    public String getDisplay()
    {
        return null;
    }
}
