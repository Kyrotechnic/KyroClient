package me.kyroclient.commands.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.commands.Command;

public class TeleportCommand extends Command {
    public TeleportCommand()
    {
        super("setteleport");
    }
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length != 4)
        {
            KyroClient.sendMessage(".teleport x y z");
            return;
        }

        KyroClient.teleportExploit.set(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        KyroClient.sendMessage("Set values for teleport exploit! Enable for next join.");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
