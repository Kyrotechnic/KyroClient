package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiJacob extends Module {
    public BooleanSetting disableNuker = new BooleanSetting("Disable Nuker", true);
    public BooleanSetting disableMacro = new BooleanSetting("Disable Macro", true);
    public BooleanSetting autoReenable = new BooleanSetting("Auto Re-enable", true);
    public static AntiJacob instance;
    public static final String jacobString = "[NPC] Jacob: The Farming Contest is over!";
    public AntiJacob()
    {
        super("Anti Jacob", Module.Category.GARDEN);

        instance = this;

        addSettings(
                disableNuker,
                disableMacro,
                autoReenable
        );
    }

    public void disable()
    {
        if (!isToggled()) return;

        if (disableMacro.isEnabled())
            KyroClient.macro.setToggled(false);

        if (disableNuker.isEnabled())
            KyroClient.cropNuker.setToggled(false);
    }

    public void reenable()
    {
        if (!isToggled()) return;
        if (!autoReenable.isEnabled()) return;

        if (disableMacro.isEnabled())
            KyroClient.macro.setToggled(true);

        if (disableNuker.isEnabled())
            KyroClient.cropNuker.setToggled(true);
    }

    @SubscribeEvent
    public void chat(PacketReceivedEvent event)
    {
        if (!isToggled()) return;
        if (event.packet instanceof S02PacketChat)
        {
            S02PacketChat packetChat = (S02PacketChat) event.packet;

            if (packetChat.getChatComponent().getUnformattedText().toLowerCase().equalsIgnoreCase(jacobString))
            {
                reenable();
            }
        }
    }
}
