package me.kyroclient.modules.misc;

import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketSniffer extends Module {
    public PacketSniffer()
    {
        super("Packet Sniffer", Category.MISC);
    }

    @SubscribeEvent
    public void onPacket(PacketSentEvent event)
    {
        if (!isToggled()) return;
        Packet packet = event.packet;

        if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive)
            return;

        sendMessage(packet.toString());
    }

    @SubscribeEvent
    public void onPacket(PacketReceivedEvent event)
    {
        if (!isToggled()) return;
        Packet packet = event.packet;

        if (packet instanceof S21PacketChunkData || packet instanceof S32PacketConfirmTransaction || packet instanceof S38PacketPlayerListItem || packet instanceof S3BPacketScoreboardObjective || packet instanceof S3CPacketUpdateScore)
            return;

        sendMessage(packet.toString());
    }
}
