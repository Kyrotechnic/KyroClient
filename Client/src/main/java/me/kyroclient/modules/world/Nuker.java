package me.kyroclient.modules.world;

import com.sun.javafx.geom.Vec3d;
import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Nuker extends Module {
    public NumberSetting range = new NumberSetting("Range", 5, 3, 6.5, 0.1);
    public BooleanSetting swingHand = new BooleanSetting("Swing Hand", false);
    public NumberSetting perTick = new NumberSetting("Per Tick", 2, 1, 4, 1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting prioritize = new BooleanSetting("Prioritize Closest", true);
    public BooleanSetting finalClick = new BooleanSetting("Sends stop break", true);
    public ModeSetting type = new ModeSetting("Blocks", "Mithril", "Gold", "Diamond");
    public Nuker()
    {
        super("Nuker", Category.WORLD);

        addSettings(range, perTick, swingHand, rotate, prioritize, finalClick);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (!isToggled()) return;
        if (KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;

        BlockPos[] poss = getCanidates((int) range.getValue());

        for (BlockPos pos : poss)
        {
            KyroClient.mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            if (swingHand.isEnabled())
                PlayerUtil.swingItem();
            if (finalClick.isEnabled())
                KyroClient.mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
        }
    }

    private BlockPos[] getCanidates(int range)
    {
        int baseX = (int) Math.min(KyroClient.mc.thePlayer.posX - range, KyroClient.mc.thePlayer.posX - range + range);
        int baseY = (int) Math.min(KyroClient.mc.thePlayer.posY - range, KyroClient.mc.thePlayer.posY + range);
        int baseZ = (int) Math.min(KyroClient.mc.thePlayer.posZ - range, KyroClient.mc.thePlayer.posZ + range);

        int maxX = (int) Math.max(KyroClient.mc.thePlayer.posX - range, KyroClient.mc.thePlayer.posX - range + range);
        int maxY = (int) Math.max(KyroClient.mc.thePlayer.posY - range, KyroClient.mc.thePlayer.posY + range);
        int maxZ = (int) Math.max(KyroClient.mc.thePlayer.posZ - range, KyroClient.mc.thePlayer.posZ + range);

        List<BlockPos> blockz = new ArrayList<>();

        for (int y = baseY; y <= maxY; y++) {
            for (int x = baseX; x <= maxX; x++) {
                for (int z = baseZ; z <= maxZ; z++) {

                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState blockState = KyroClient.mc.theWorld.getBlockState(pos);
                    Block block = blockState.getBlock();

                    if (toBreak(block, blockState))
                        blockz.add(pos);
                }
            }
        }

        blockz.sort(Comparator.comparingDouble(c -> KyroClient.mc.thePlayer.getDistanceSqToCenter(c)));
        Collections.reverse(blockz);
        BlockPos[] blockpos = new BlockPos[(int) perTick.getValue()];
        for (int i = 0; i < (int) perTick.getValue(); i++)
        {
            blockpos[i] = blockz.get(i);
        }

        return blockpos;
    }

    public boolean toBreak(Block block, IBlockState blockState)
    {
        switch (type.getSelected())
        {
            case "Mithril":
                int metadata = block.getMetaFromState(blockState);

                if (block == Blocks.prismarine || (block == Blocks.wool && metadata == 7) || (block == Blocks.stained_hardened_clay && metadata == 9))
                {
                    return true;
                }
                return false;
            case "Gold":
                return (block == Blocks.gold_block);
            case "Diamond":
                return (block == Blocks.diamond_block);
            default:
                return false;
        }
    }
}
