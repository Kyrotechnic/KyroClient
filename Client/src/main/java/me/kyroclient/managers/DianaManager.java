package me.kyroclient.managers;

import net.minecraft.util.BlockPos;

import java.util.concurrent.CopyOnWriteArrayList;

public class DianaManager {
    public CopyOnWriteArrayList<BlockPos> diana_blocks = new CopyOnWriteArrayList<>();

    public static void init()
    {
        //Add default pos (not added)
    }
}
