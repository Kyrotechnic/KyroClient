package me.kyroclient.util;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class LagPacket implements Packet {
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

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        throw new IOException("Not a real packet!");
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        packet.writePacketData(buf);
    }

    @Override
    public void processPacket(INetHandler handler) {

    }
}
