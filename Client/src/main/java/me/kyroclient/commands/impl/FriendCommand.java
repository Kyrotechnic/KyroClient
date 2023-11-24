package me.kyroclient.commands.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.commands.Command;

import java.util.stream.Collectors;

public class FriendCommand extends Command {
    public FriendCommand()
    {
        super("friend");
    }
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length != 3)
        {
            printFriends();
            return;
        }

        switch (args[1])
        {
            case "add":
                KyroClient.friendManager.add(args[2]);
                KyroClient.sendMessage("§aAdded " + args[2] + " as a friend!");
                break;
            case "remove":
                KyroClient.friendManager.remove(args[2]);
                KyroClient.sendMessage("§cRemoved §a" + args[2] + " as a friend.");
                break;
            default:
                printFriends();
                break;
        }
    }

    @Override
    public String getDescription() {
        return "Add or remove friends!";
    }

    public void printFriends()
    {
        KyroClient.sendMessage("§aFriends: §c" + KyroClient.friendManager.playerUUID.values().stream().collect(Collectors.joining("§9, §c")));
    }
}
