package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;

public class NoFallingBlocks extends Module {
    public NoFallingBlocks()
    {
        super("No Falling Blocks", Category.MISC);
    }

    @Override
    public void assign()
    {
        KyroClient.noFallingBlocks = this;
    }
}
