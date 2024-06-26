package me.kyroclient.modules.mining;

import lombok.SneakyThrows;
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
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class BlockFricker extends Module {
    public NumberSetting range = new NumberSetting("Range", 5, 3, 6.5, 0.1);
    public BooleanSetting swingHand = new BooleanSetting("Swing Hand", false);
    public NumberSetting perTick = new NumberSetting("Per Tick", 2, 1, 10, 1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting prioritize = new BooleanSetting("Prioritize Closest", true);
    public BooleanSetting finalClick = new BooleanSetting("Sends stop break", true);
    public BooleanSetting removeAfter = new BooleanSetting("Remove", true);
    public ModeSetting type = new ModeSetting("Blocks", "Mithril", "Mithril", "Gold", "Diamond", "Mycelium", "Red Sand", "Quartz", "Netherrack", "Foraging", "Custom");
    public static File file = new File(KyroClient.mc.mcDataDir + "/config/KyroClient/BlockFricker.cfg");
    @SneakyThrows
    public BlockFricker()
    {
        super("Block Fricker", Category.MINING);

        addSettings(type, range, perTick, swingHand, rotate, prioritize, finalClick, removeAfter);

        if (!file.exists())
        {
            Files.createFile(file.toPath());
        }

        load();
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

            if (removeAfter.isEnabled())
                KyroClient.mc.theWorld.setBlockToAir(pos);
        }
    }

    private BlockPos[] getCanidates(int range)
    {
        List<BlockPos> blockz = new ArrayList<>();

        Vec3 playerVec = KyroClient.mc.thePlayer.getPositionVector();
        BlockPos playerPos = KyroClient.mc.thePlayer.getPosition();
        Vec3i vec3i = new Vec3i(range, range, range);

        for (BlockPos pos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)))
        {
            IBlockState state = KyroClient.mc.theWorld.getBlockState(pos);
            if (toBreak(state.getBlock(), state))
                blockz.add(pos);
        }

        if (blockz.isEmpty()) return new BlockPos[0];

        blockz.sort(Comparator.comparingDouble(c -> KyroClient.mc.thePlayer.getDistanceSqToCenter(c)));
        //Collections.reverse(blockz);
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
            case "Mycelium":
                return (block == Blocks.mycelium);
            case "Red Sand":
                int meta = block.getMetaFromState(blockState);

                return (block == Blocks.sand && meta == 1);
            case "Quartz":
                return (block == Blocks.quartz_ore);
            case "Netherrack":
                return (block == Blocks.netherrack);
            case "Foraging":
                return (block == Blocks.log || block == Blocks.log2);
            case "Custom":
                return customBlocks.contains(block);
        }

        return false;
    }

    public static List<Block> customBlocks = new ArrayList<>();

    public static void addBlock(Block block)
    {
        if (customBlocks.contains(block))
            return;
        customBlocks.add(block);
        save();
    }

    @SneakyThrows
    public static void save()
    {
        String[] str = new String[customBlocks.size()];

        int i = 0;

        for (Block block : customBlocks)
        {
            str[i] = block.getRegistryName();
            i++;
        }

        Files.write(file.toPath(), Arrays.asList(str));
    }

    public static Block getBlock(String id)
    {
        Block block = Block.getBlockFromName(id);

        return block;
    }

    @SneakyThrows
    public static void load()
    {
        customBlocks.clear();
        List<String> strs = Files.readAllLines(file.toPath());

        for (String string : strs)
        {
            customBlocks.add(getBlock(string));
        }
    }

    public static void removeBlock(Block block)
    {
        if (!customBlocks.contains(block))
            return;
        customBlocks.remove(block);
        save();
    }
}
