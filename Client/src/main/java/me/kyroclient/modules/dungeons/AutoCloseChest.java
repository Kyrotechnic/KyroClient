package me.kyroclient.modules.dungeons;

import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.util.PacketUtils;
import me.kyroclient.util.SkyblockArea;
import me.kyroclient.util.SkyblockUtils;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCloseChest extends Module {
    public AutoCloseChest()
    {
        super("Auto Close Chest", Category.DUNGEONS);
    }

    @SubscribeEvent
    public void close(PacketReceivedEvent event)
    {
        if (isToggled() && SkyblockUtils.currentArea == SkyblockArea.DUNGEON && event.packet instanceof S2DPacketOpenWindow)
        {
            S2DPacketOpenWindow window = (S2DPacketOpenWindow) event.packet;

            if (window.getWindowTitle().getUnformattedText() == "Chest")
            {
                event.setCanceled(true);
                PacketUtils.sendPacket(new C0DPacketCloseWindow(window.getWindowId()));
            }
        }
    }
}
