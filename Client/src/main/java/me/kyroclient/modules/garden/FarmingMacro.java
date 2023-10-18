package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FarmingMacro extends Module {
    public BooleanSetting defaultDirection = new BooleanSetting("Default left?", true);
    public boolean direction;
    public boolean aDirection;
    public double prevX;
    public double prevY;
    public double prevZ;
    public int tickCounter;
    public FarmState state;
    public boolean changed;
    public int secondTick;
    public FarmingMacro()
    {
        super("Farming Macro", Category.GARDEN);
    }

    @Override
    public void onEnable()
    {
        direction = defaultDirection.isEnabled();
        aDirection = true;
        changed = false;
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindAttack.getKeyCode(), true);
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindLeft.getKeyCode(), direction);
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindRight.getKeyCode(), !direction);

        prevX = 0;
        prevY = 0;
        prevZ = 0;

        tickCounter = 0;
        secondTick = 0;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e)
    {
        if (!isToggled()) return;

        if (KyroClient.mc.currentScreen != null)
        {
            toggle();
            return;
        }
        if (secondTick > 0)
        {
            secondTick--;
        }

        if (tickCounter > 0)
        {
            tickCounter--;
        }
        else if (tickCounter == 0 && state == FarmState.CHANGELAYER)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindBack.getKeyCode(), false);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), false);
            direction = !direction;

            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindRight.getKeyCode(), !direction);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindLeft.getKeyCode(), direction);

            state = FarmState.FARMING;

            tickCounter = 3;

            changed = false;
        }
        else if (tickCounter == 0 && state == FarmState.FARMING)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), true);

            changed = false;

            secondTick = 3;
        }

        if (secondTick == 0)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), false);
        }

        if (prevZ == KyroClient.mc.thePlayer.posZ && !changed)
        {
            state = FarmState.CHANGELAYER;

            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindAttack.getKeyCode(), false);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindBack.getKeyCode(), aDirection);
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), !aDirection);

            tickCounter = 25;
            aDirection = !aDirection;
            changed = true;
        }

        prevX = KyroClient.mc.thePlayer.posX;
        prevY = KyroClient.mc.thePlayer.posY;
        prevZ = KyroClient.mc.thePlayer.posZ;
    }

    @Override
    public void onDisable()
    {
        direction = false;
        state = FarmState.FARMING;
        prevX = 0;
        prevY = 0;
        prevZ = 0;
        tickCounter = 0;
        secondTick = 0;
        changed = false;
    }

    public enum FarmState
    {
        FARMING,
        CHANGELAYER;
    }
}
