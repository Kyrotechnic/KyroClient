package me.kyroclient.util;

import me.kyroclient.KyroClient;
import net.minecraft.network.Packet;

public class PacketUtils {
    public static void sendPacketNoEvent(Packet packet)
    {
        KyroClient.mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
