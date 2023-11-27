package me.kyroclient.commands.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.commands.Command;
import me.kyroclient.modules.combat.Aura;

public class AuraCommand extends Command {
    public AuraCommand()
    {
        super("aura");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 3)
        {
            KyroClient.sendMessage(".aura <add/remove> <name contains>");
            return;
        }

        if (args[1] == "add")
        {

        }
    }

    @Override
    public String getDescription() {
        return null;
    }
}
