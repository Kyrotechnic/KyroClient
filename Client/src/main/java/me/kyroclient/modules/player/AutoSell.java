package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.settings.StringSetting;
import me.kyroclient.util.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoSell extends Module {
    public NumberSetting delay = new NumberSetting("Tick Delay", 2, 1, 20, 1);
    public StringSetting extensive = new StringSetting("Title", "bazaar");
    public AutoSell()
    {
        super("Auto Sell", Category.PLAYER);

        addSettings(delay, extensive);
    }

    public boolean isInGui = false;
    public int windowId = -1;
    public int ticks = 0;

    @Override
    public void onEnable()
    {
        isInGui = false;
        windowId = -1;
    }

    @Override
    public void onDisable()
    {
        isInGui = false;
        windowId = -1;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;

        if (isInGui)
        {
            if (ticks == delay.getValue())
            {
                PacketUtils.sendPacket(new C0EPacketClickWindow(windowId, 47, 0, 0, null, (short) 0));
                ticks = 0;
            }
            ticks++;
        }
    }

    @SubscribeEvent
    public void packet(PacketReceivedEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;

        Packet packet = event.packet;
        if (packet instanceof S2DPacketOpenWindow)
        {
            S2DPacketOpenWindow packetOpenWindow = (S2DPacketOpenWindow) packet;

            if (packetOpenWindow.getWindowTitle().getUnformattedText().toLowerCase().contains(extensive.getValue()))
            {
                isInGui = true;
                windowId = packetOpenWindow.getWindowId();
                event.setCanceled(true);
            }
        }
    }
}
