package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.StringSetting;

public class NickHider extends Module {
    public StringSetting nick;
    public NickHider()
    {
        super("Nick Hider", Category.PLAYER);

        addSettings(
                nick = new StringSetting("Name", KyroClient.mc.getSession().getUsername())
        );
    }

    @Override
    public void assign()
    {
        KyroClient.nickHider = this;
    }

    @Override
    public String suffix()
    {
        return nick.getValue();
    }
}
