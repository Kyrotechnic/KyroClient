package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.LagPacket;
import me.kyroclient.util.PacketUtils;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class FakeLag extends Module {
    public NumberSetting delay = new NumberSetting("Delay MS", 20, 0, 300, 5);
    public CopyOnWriteArrayList<LagPacket> packets = new CopyOnWriteArrayList<>();
    public FakeLag()
    {
        super("Fake Lag", Category.PLAYER);
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
        boolean flag = true;

        if (event.packet instanceof LagPacket)
            flag = false;

        if (!(event.packet instanceof C00PacketKeepAlive) && !(event.packet instanceof C0FPacketConfirmTransaction) && flag)
        {
            event.setCanceled(true);

            packets.add(new LagPacket(event.packet, (long) delay.getValue()));
        }

        send();
    }

    public void send()
    {
        for (LagPacket packet : packets)
        {
            if (!packet.send()) continue;

            PacketUtils.sendPacketNoEvent(packet.packet);
            packets.remove(packet);
        }
    }

    @Override
    public void onDisable()
    {
        for (LagPacket packet : packets)
        {
            PacketUtils.sendPacketNoEvent(packet.packet);
        }
    }
}
