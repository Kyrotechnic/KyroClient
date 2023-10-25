package me.kyroclient.util;

import net.minecraft.network.Packet;

public class LagPacket {
    public Packet packet;
    private MilliTimer initial;
    private long delay;
    public LagPacket(Packet packet, long delay)
    {
        this.packet = packet;
        initial = new MilliTimer();
        initial.reset();
        this.delay = delay;
    }

    public boolean send()
    {
        if (initial.hasTimePassed(delay))
            return true;
        return false;
    }
}
