package me.kyroclient.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.kyroclient.events.JoinGameEvent;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.player.FakeLag;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Shadow public abstract void sendPacket(Packet packetIn);

    private boolean fakePacket = false;
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true, at = @At("HEAD"))
    public void sendPacket(Packet packet, CallbackInfo ci)
    {
        if (FakeLag.isFlushing()) return;

        if (MinecraftForge.EVENT_BUS.post(new PacketSentEvent(packet)))
        {
            ci.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true, at = @At("RETURN"))
    public void sendPacketPost(Packet packet, CallbackInfo ci)
    {
        if (MinecraftForge.EVENT_BUS.post(new PacketSentEvent.Post(packet)))
            ci.cancel();
    }

    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelReadHead(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (packet instanceof S01PacketJoinGame) {
            MinecraftForge.EVENT_BUS.post(new JoinGameEvent());
        }
        if (MinecraftForge.EVENT_BUS.post(new PacketReceivedEvent(packet))) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = { "channelRead0" }, at = { @At("RETURN") }, cancellable = true)
    private void onPost(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post(new PacketReceivedEvent.Post(packet))) {
            callbackInfo.cancel();
        }
    }
}
