package me.kyroclient.util;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class SkyblockUtils {
    public static final String FAIRY_SOUL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk2OTIzYWQyNDczMTAwMDdmNmFlNWQzMjZkODQ3YWQ1Mzg2NGNmMTZjMzU2NWExODFkYzhlNmIyMGJlMjM4NyJ9fX0=";
    public static String getDisplayName(final ItemStack item) {
        if (item == null) {
            return "null";
        }
        return item.getDisplayName();
    }

    public static boolean isFairySoul(EntityArmorStand entity)
    {
        ItemStack helmet = entity.getCurrentArmor(3);
        if (helmet == null || !(helmet.getItem() instanceof ItemSkull)) return false;

        NBTTagList tags = helmet.serializeNBT().getCompoundTag("tag").getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tags.tagCount(); i++)
        {
            if (tags.getCompoundTagAt(i).getString("Value").equals(FAIRY_SOUL_TEXTURE)) return true;
        }

        return false;
    }

}
