package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MilliTimer;
import me.kyroclient.util.PacketUtils;
import me.kyroclient.util.TypeUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.CopyOnWriteArrayList;

public class FakeLag extends Module {
    public NumberSetting delay = new NumberSetting("Delay MS", 20, 0, 300, 5);
    public BooleanSetting onlyKeepAlive = new BooleanSetting("Only Keep Alive", true);
    public CopyOnWriteArrayList<Packet> packets = new CopyOnWriteArrayList<>();
    public static boolean flush = false;
    public MilliTimer timer = new MilliTimer();
    public FakeLag()
    {
        super("Fake Lag", Category.PLAYER);

        addSettings(delay, onlyKeepAlive);
    }

    @Override
    public void onEnable()
    {
        packets.clear();
        flush = false;
        timer.reset();
    }

    @SubscribeEvent
    public void packet(PacketSentEvent event)
    {
        if (!isToggled()) return;

        if (TypeUtils.isof(event.packet, C0FPacketConfirmTransaction.class))
        {
            return;
        }

        if (onlyKeepAlive.isEnabled() && !TypeUtils.isnof(event.packet, C00PacketKeepAlive.class))
        {
            return;
        }

        packets.add(event.packet);

        if (timer.hasTimePassed((long) delay.getValue()))
        {
            send();
        }
    }

    @Override
    public void assign()
    {
        KyroClient.fakeLag = this;
    }

    public static boolean isFlushing()
    {
        return KyroClient.fakeLag.isToggled() && flush;
    }

    public void send()
    {
        flush = true;
        timer.reset();

        for (Packet packet : packets)
        {
            PacketUtils.sendPacket(packet);
        }
        packets.clear();

        flush = false;
    }

    @Override
    public void onDisable()
    {
        send();
        packets.clear();
    }
}
