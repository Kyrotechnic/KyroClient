package me.kyroclient.modules.diana;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DianaHelper extends Module {
    public BooleanSetting burrowHelper = new BooleanSetting("Burrow Helper", true);
    public DianaHelper()
    {
        super("Diana Helper", Category.DIANA);

        //S2AParticles
        //Goes to center of block
        //Goes from eyes to center of target block

        addSettings(
                burrowHelper
        );
    }

    public boolean search = false;
    public BlockPos lastParticlePos;
    public BlockPos lastParticlePos2;
    @Override
    public void onEnable()
    {
        search = false;
    }

    @Override
    public void onDisable()
    {
        search = false;
    }

    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;
        if (KyroClient.mc.thePlayer.getHeldItem() == null) return;
    }
}
