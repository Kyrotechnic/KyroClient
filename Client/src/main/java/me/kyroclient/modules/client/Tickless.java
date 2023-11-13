package me.kyroclient.modules.client;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;

public class Tickless extends Module {
    public BooleanSetting outPacket = new BooleanSetting("Outbound Packets", true);
    public BooleanSetting movement = new BooleanSetting("Movement", false);
    public BooleanSetting inPacket = new BooleanSetting("Inbound Packets", true);
    public Tickless()
    {
        super("Tickless", Category.CLIENT);

        addSettings(
            outPacket,
            movement,
            inPacket
        );
    }

    @Override
    public void assign()
    {
        KyroClient.tickless = this;
    }
}
