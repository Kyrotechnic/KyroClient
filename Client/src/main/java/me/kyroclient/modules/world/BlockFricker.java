package me.kyroclient.modules.world;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.PlayerUtil;
import net.minecraft.block.Block;
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

public class BlockFricker extends Module {
    public NumberSetting range = new NumberSetting("Range", 5, 3, 6.5, 0.1);
    public BooleanSetting swingHand = new BooleanSetting("Swing Hand", false);
    public NumberSetting perTick = new NumberSetting("Per Tick", 2, 1, 10, 1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting prioritize = new BooleanSetting("Prioritize Closest", true);
    public BooleanSetting finalClick = new BooleanSetting("Sends stop break", true);
    public ModeSetting type = new ModeSetting("Blocks", "Mithril", "Mithril", "Gold", "Diamond");
    public BlockFricker()
    {
        super("Block Fricker", Category.WORLD);

        addSettings(type, range, perTick, swingHand, rotate, prioritize, finalClick);
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
        List<BlockPos> blockz = new ArrayList<>();

        /*for (int y = baseY; y <= maxY; y++) {
            for (int x = baseX; x <= maxX; x++) {
                for (int z = baseZ; z <= maxZ; z++) {

                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState blockState = KyroClient.mc.theWorld.getBlockState(pos);
                    Block block = blockState.getBlock();

                    if (toBreak(block, blockState))
                        blockz.add(pos);
                }
            }
        }*/

        int playerX = (int) KyroClient.mc.thePlayer.posX;
        int playerY = (int) KyroClient.mc.thePlayer.posY;
        int playerZ = (int) KyroClient.mc.thePlayer.posZ;

        for (int x = playerX - range; x <= playerX + range; x++)
        {
            for (int z = playerZ - range; z <= playerZ + range; z++)
            {
                for (int y = Math.max(0, playerY - range); y <= playerY + range; y++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState blockState = KyroClient.mc.theWorld.getBlockState(pos);
                    Block block = blockState.getBlock();

                    if (toBreak(block, blockState))
                        blockz.add(pos);
                }
            }
        }

        if (blockz.isEmpty()) return new BlockPos[0];

        blockz.sort(Comparator.comparingDouble(c -> KyroClient.mc.thePlayer.getDistanceSqToCenter(c)));
        Collections.reverse(blockz);
        if (blockz.size() == 0) return new BlockPos[0];
        int max = (int) Math.min((int) perTick.getValue(), blockz.size());
        BlockPos[] blockpos = new BlockPos[max];
        if (max == 0) return new BlockPos[0];
        for (int i = 0; i < max; i++)
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
