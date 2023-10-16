package me.kyroclient.modules.player;

import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;

public class ServerRotations extends Module
{
    private static ServerRotations instance;
    public BooleanSetting onlyKillAura;

    public static ServerRotations getInstance() {
        return ServerRotations.instance;
    }

    public ServerRotations() {
        super("Server Rotations", Category.RENDER);
        this.onlyKillAura = new BooleanSetting("Only aura rotations", false);
        this.setToggled(true);
        this.addSettings(this.onlyKillAura);
    }

    static {
        ServerRotations.instance = new ServerRotations();
    }
}
