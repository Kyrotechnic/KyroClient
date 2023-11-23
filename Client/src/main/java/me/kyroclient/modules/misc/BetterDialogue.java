package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.ModeSetting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class BetterDialogue extends Module {
    public ModeSetting npcMode = new ModeSetting("Interact Mode", "Crouch", "Crouch", "Left Click");
    public BetterDialogue()
    {
        super("Better Dialogue", Category.MISC);

        addSettings(
                npcMode
        );
    }

    public static String acceptCommand = null;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event)
    {
        if (!isToggled() || event.message == null || event.type == 2) return;
        if (!StringUtils.stripControlCodes(event.message.getUnformattedText()).startsWith("Select an option: ")) return;
        IChatComponent component = event.message.getSiblings().get(0);
        acceptCommand = component.getChatStyle().getChatClickEvent().getValue();
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null || acceptCommand == null) return;

        boolean flag = false;

        switch (npcMode.getSelected())
        {
            case "Crouch":
                flag = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                break;
            case "Left Click":
                flag = KyroClient.mc.gameSettings.keyBindAttack.isKeyDown();
                break;
        }

        if (flag)
        {
            KyroClient.mc.thePlayer.sendChatMessage(acceptCommand);
            acceptCommand = null;
        }
    }
}
