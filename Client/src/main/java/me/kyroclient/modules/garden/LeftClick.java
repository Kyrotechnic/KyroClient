package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MotionUpdateEvent;
import me.kyroclient.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LeftClick extends Module {
    public LeftClick()
    {
        super("Left Click", Category.GARDEN);
    }

    @Override
    public void onEnable()
    {

    }
    @Override
    public void onDisable()
    {
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }

    @SubscribeEvent
    public void onMove(MotionUpdateEvent.Pre event)
    {
        if (isToggled())
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindAttack.getKeyCode(), true);
    }
}
