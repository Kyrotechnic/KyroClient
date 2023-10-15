package me.kyroclient.util;

import net.minecraft.item.ItemStack;

public class SkyblockUtils {
    public static String getDisplayName(final ItemStack item) {
        if (item == null) {
            return "null";
        }
        return item.getDisplayName();
    }
}
