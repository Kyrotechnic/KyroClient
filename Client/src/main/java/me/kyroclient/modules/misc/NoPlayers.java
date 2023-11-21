package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.events.RenderEntityEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.combat.AntiBot;
import me.kyroclient.settings.BooleanSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class NoPlayers extends Module {
    public BooleanSetting hidePartyMembers = new BooleanSetting("Hide Party", false);
    public BooleanSetting hideNpc = new BooleanSetting("Hide NPC", false);
    public NoPlayers()
    {
        super("Hide Players", Category.MISC);

        addSettings(
            hidePartyMembers,
            hideNpc
        );
    }

    @SubscribeEvent
    public void tick(RenderPlayerEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null)
        {
            if (event.entityPlayer != KyroClient.mc.thePlayer && event.entityPlayer instanceof EntityPlayer)
            {
                if (!KyroClient.friendManager.has(event.entity.getDisplayName().getUnformattedText()) && AntiBot.isValidEntity(event.entity))
                    event.setCanceled(true);
            }
        }
    }
}
