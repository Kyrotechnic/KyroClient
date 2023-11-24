package me.kyroclient.modules.dungeons;

import me.kyroclient.KyroClient;
import me.kyroclient.events.BlockChangeEvent;
import me.kyroclient.events.JoinGameEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class GhostBlock extends Module {
    public ModeSetting speed = new ModeSetting("Speed", "Fast", "Slow", "Fast");
    public NumberSetting range = new NumberSetting("Range", 5, 3, 30, 1);
    public GhostBlock()
    {
        super("Ghost Block", Category.DUNGEONS);

        addSettings(
                speed,
                range
        );
    }

    public List<BlockPos> ghostBlocks = new ArrayList<>();

    @SubscribeEvent
    public void onWorldJoin(final WorldEvent.Load event) {
        ghostBlocks.clear();
    }



    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;

        boolean flag = true;
        if (speed.is("Slow"))
            flag = KyroClient.ticks % 5 == 0;

        if (!Keyboard.isKeyDown(getKeycode()))
        {
            setToggled(false);
            return;
        }

        if (flag)
        {
            Vec3 vec3 = KyroClient.mc.thePlayer.getPositionEyes(0);
            Vec3 vec4 = KyroClient.mc.thePlayer.getLook(0);
            Vec3 vec5 = vec3.addVector(vec4.xCoord * this.range.getValue(), vec4.yCoord * this.range.getValue(), vec4.zCoord * this.range.getValue());

            BlockPos pos = KyroClient.mc.theWorld.rayTraceBlocks(vec3, vec5, true, false, true).getBlockPos();

            if (!validBlock(pos))
            {
                KyroClient.mc.theWorld.setBlockToAir(pos);
                ghostBlocks.add(pos);
            }
        }
    }

    public boolean validBlock(BlockPos pos)
    {
        Block block = KyroClient.mc.theWorld.getBlockState(pos).getBlock();

        return block == Blocks.lever || block == Blocks.chest || block == Blocks.trapped_chest;
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event)
    {
        if (event.state != null && ghostBlocks.contains(event.pos) && isToggled() && event.state.getBlock() != Blocks.air)
            event.setCanceled(true);
    }
}
