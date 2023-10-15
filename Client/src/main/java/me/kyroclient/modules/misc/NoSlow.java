package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MotionUpdateEvent;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MilliTimer;
import me.kyroclient.util.PacketUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {
    public NumberSetting eatingSlowdown;
    public NumberSetting swordSlowdown;
    public NumberSetting bowSlowdown;
    public ModeSetting mode;
    private final MilliTimer blockDelay;

    public NoSlow() {
        super("NoSlow", 0, Category.COMBAT);
        this.eatingSlowdown = new NumberSetting("Eating slow", 1.0, 0.2, 1.0, 0.1);
        this.swordSlowdown = new NumberSetting("Sword slow", 1.0, 0.2, 1.0, 0.1);
        this.bowSlowdown = new NumberSetting("Bow slow", 1.0, 0.2, 1.0, 0.1);
        this.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel", "Vanilla" });
        this.blockDelay = new MilliTimer();
        this.addSettings(this.mode, this.swordSlowdown, this.bowSlowdown, this.eatingSlowdown);
    }

    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S30PacketWindowItems && KyroClient.mc.thePlayer != null && this.isToggled() && this.mode.is("Hypixel") && KyroClient.mc.thePlayer.isUsingItem() && KyroClient.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void unUpdate(final MotionUpdateEvent.Post event) {
        if (this.isToggled() && KyroClient.mc.thePlayer.isUsingItem() && this.mode.is("Hypixel")) {
            if (this.blockDelay.hasTimePassed(250L) && KyroClient.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) {
                KyroClient.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(KyroClient.mc.thePlayer.getHeldItem()));
                KyroClient.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction(KyroClient.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                KyroClient.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction(KyroClient.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                this.blockDelay.reset();
            }
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(KyroClient.mc.thePlayer.inventory.currentItem));
        }
    }

    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && this.mode.is("Hypixel") && event.packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)event.packet).getStack() != null && ((C08PacketPlayerBlockPlacement)event.packet).getStack().getItem() instanceof ItemSword) {
            this.blockDelay.reset();
        }
    }
}
