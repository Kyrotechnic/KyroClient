package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.events.JoinGameEvent;
import me.kyroclient.events.PacketReceivedEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FarmingMacro extends Module {
    public BooleanSetting defaultDirection = new BooleanSetting("Default left?", true);
    public BooleanSetting disableOnWorld = new BooleanSetting("Disable on Join", true);
    public BooleanSetting rotationSafe = new BooleanSetting("Rotation safe", true);
    public BooleanSetting stopOnGui = new BooleanSetting("Stop on GUI", true);
    public FarmState farmState;
    public int tickCounter;
    public int failsafeCounter;
    public FarmingMacro()
    {
        super("Farming Macro", Category.GARDEN);

        addSettings(
                defaultDirection,
                disableOnWorld,
                rotationSafe,
                stopOnGui
        );
    }

    @SubscribeEvent
    public void packet(PacketReceivedEvent e)
    {
        if ((rotationSafe.isEnabled()) && e.packet instanceof S08PacketPlayerPosLook)
        {
            if (KyroClient.mc.thePlayer.posY < 30)
            {
                sendMessage("Congrats, you farmed the entire farm!");
            }
            else
            {
                sendMessage("STAFF TELEPORT OR ROTATION! STOPPING");
                setToggled(false);
                return;
            }

            failsafeCounter = 100;
        }
    }

    @Override
    public void onEnable()
    {
        if (defaultDirection.isEnabled())
            farmState = FarmState.GOINGLEFT;
        else
            farmState = FarmState.GOINGRIGHT;

        tickCounter = 0;
        failsafeCounter = 0;

        updateState(farmState);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e)
    {
        if (!isToggled()) return;
        if (stopOnGui.isEnabled() && KyroClient.mc.currentScreen != null)
        {
            setToggled(false);
            sendMessage("GUI OPEN! STOPPING");
            return;
        }

        BlockPos playerPos = new BlockPos(KyroClient.mc.thePlayer.posX, KyroClient.mc.thePlayer.posY, KyroClient.mc.thePlayer.posZ);
        BlockPos shiftLeft = playerPos.add(0, 0, -1);
        BlockPos shiftRight = playerPos.add(0, 0, 1);
        BlockPos shiftDown = playerPos.add(0, -1, 0);

        failsafeCounter--;
        IBlockState shiftedDown = KyroClient.mc.theWorld.getBlockState(shiftDown);

        if (failsafeCounter > 0 && shiftedDown.getBlock() == Blocks.bedrock)
        {
            sendMessage("FALSE TELEPORT STAFF HERE");
            setToggled(false);
            return;
        }

        IBlockState shiftedLeft = KyroClient.mc.theWorld.getBlockState(shiftLeft);
        IBlockState shiftedRight = KyroClient.mc.theWorld.getBlockState(shiftRight);

        boolean updateState = false;

        switch (farmState)
        {
            case GOINGBACK:
                if (shiftedLeft.getBlock() == Blocks.end_stone)
                {
                    farmState = FarmState.GOINGRIGHT;
                    updateState(farmState);
                    tickCounter = 15;
                }
                break;
            case GOINGFORWARD:
                if (shiftedRight.getBlock() == Blocks.end_stone)
                {
                    farmState = FarmState.GOINGLEFT;
                    updateState(farmState);
                    tickCounter = 15;
                }
                break;
            case GOINGLEFT:
                if (shiftedLeft.getBlock() == Blocks.obsidian)
                {
                    farmState = FarmState.GOINGBACK;
                    updateState(farmState);
                }
                break;
            case GOINGRIGHT:
                if (shiftedRight.getBlock() == Blocks.obsidian)
                {
                    farmState = FarmState.GOINGFORWARD;
                    updateState(farmState);
                }
                break;
        }

        if (tickCounter > 0 && farmState == FarmState.GOINGRIGHT)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
        else if (tickCounter == 0 && farmState == FarmState.GOINGRIGHT)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), false);
        }

        if (tickCounter > 0 && farmState == FarmState.GOINGLEFT)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
        else if (tickCounter == 0 && farmState == FarmState.GOINGLEFT)
        {
            KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), false);
        }

        tickCounter--;
    }

    public void updateState(FarmState state)
    {
        switch (farmState)
        {
            case GOINGBACK:
                updateBinds(false, true, false, false, false);
                break;
            case GOINGFORWARD:
                updateBinds(true, false, false, false, false);
                break;
            case GOINGLEFT:
                updateBinds(false, false, true, false, true);
                break;
            case GOINGRIGHT:
                updateBinds(false, false, false, true, true);
                break;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load e)
    {
        if (disableOnWorld.isEnabled())
            setToggled(false);
    }

    public void updateBinds(boolean forward, boolean back, boolean left, boolean right, boolean attack)
    {
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindForward.getKeyCode(), forward);
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindBack.getKeyCode(), back);
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindLeft.getKeyCode(), left);
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindRight.getKeyCode(), right);
        KeyBinding.setKeyBindState(KyroClient.mc.gameSettings.keyBindAttack.getKeyCode(), attack);
    }

    @Override
    public void onDisable()
    {
        tickCounter = 0;
        failsafeCounter = 0;
        updateBinds(false, false, false, false, false);
    }

    public enum FarmState
    {
        GOINGLEFT,
        GOINGRIGHT,
        GOINGBACK,
        GOINGFORWARD;
    }
}