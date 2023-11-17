package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.CopyOnWriteArrayList;

public class FakeLag extends Module {
    public NumberSetting delay = new NumberSetting("Delay MS", 20, 0, 300, 5);
    public CopyOnWriteArrayList<Packet> packets = new CopyOnWriteArrayList<>();
    public FakeLag()
    {
        super("Fake Lag", Category.PLAYER);

        addSettings(delay);
    }

    @Override
    public void onEnable()
    {
        packets.clear();
    }

    @SubscribeEvent
    public void packet(PacketSentEvent event)
    {
        if (!isToggled()) return;
    }

    public void send()
    {

    }

    @Override
    public void onDisable()
    {
        for (Packet packet : packets)
        {
            PacketUtils.sendPacket(packet);
        }
    }
}
