package me.kyroclient.commands.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.commands.Command;
import me.kyroclient.modules.mining.BlockFricker;
import net.minecraft.block.Block;

import java.util.Locale;

public class NukerCommand extends Command {
    public NukerCommand()
    {
        super("nuker");
    }
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 1 || args.length == 2)
        {
            KyroClient.sendMessage(".nuker add/remove block_id");
            return;
        }

        Block block = getBlock(args[3]);

        if (block == null) {
            invalid(args[3]);
            return;
        }

        switch (args[2])
        {
            case "add":
                BlockFricker.addBlock(block);
                KyroClient.sendMessage("Succesfully added block of type " + args[3]);
                break;
            case "remove":
                BlockFricker.removeBlock(block);
                KyroClient.sendMessage("Succesfully removed block of type " + args[3]);
                break;
        }
    }

    public void invalid(String id)
    {
        KyroClient.sendMessage(id + " is an invalid block ID!");
    }

    public Block getBlock(String id)
    {
        Block block = Block.getBlockFromName(id.toLowerCase());

        return block;
    }

    @Override
    public String getDescription() {
        return "BlockFricker interface command";
    }
}
