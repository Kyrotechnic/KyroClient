package me.kyroclient.util;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import kotlin.text.Regex;
import me.kyroclient.KyroClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.StringUtils;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public static String getSkyblockItemID(ItemStack item) {
        if (item == null) {
            return null;
        }

        NBTTagCompound extraAttributes = getExtraAttributes(item);
        if (extraAttributes == null) {
            return null;
        }

        if (!extraAttributes.hasKey("id", SkyblockUtils.NBT_STRING)) {
            return null;
        }

        return extraAttributes.getString("id");
    }

    public static final int NBT_INTEGER = 3;
    public static final int NBT_STRING = 8;
    public static final int NBT_LIST = 9;

    public static SkyblockArea currentArea = null;
    public static int ticks;
    public static Regex areaRegex = new Regex("^(?:Area|Dungeon): ([\\w ].+)\\$");
    public static void tick(TickEvent.ClientTickEvent event)
    {
        if (!PlayerUtil.isOnSkyBlock() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null || event.phase != TickEvent.Phase.START) return;

        if (ticks % 10 == 0)
        {
            List<NetworkPlayerInfo> info = fetchTabList();

            String areaString = null;

            for (NetworkPlayerInfo playerInfo : info)
            {
                if (areaRegex.find(playerInfo.getDisplayName().getUnformattedText(), 0) != null)
                {
                    areaString = areaRegex.find(playerInfo.getDisplayName().getUnformattedText(), 0).getValue();
                }

                currentArea = SkyblockArea.getArea(areaString);
            }
        }
        ticks++;
    }

    public static Ordering<NetworkPlayerInfo> playerInfoOrdering = new Ordering<NetworkPlayerInfo>() {
        @Override
        public int compare(@Nullable NetworkPlayerInfo left, @Nullable NetworkPlayerInfo right) {
            ScorePlayerTeam team1 = left.getPlayerTeam();
            ScorePlayerTeam team2 = right.getPlayerTeam();

            if (team1 != null)
            {
                if (team2 != null)
                {
                    return ComparisonChain.start().compareTrueFirst(
                            left.getGameType() != WorldSettings.GameType.SPECTATOR,
                            right.getGameType() != WorldSettings.GameType.SPECTATOR
                    ).compare(
                            team1.getRegisteredName(),
                            team2.getRegisteredName()
                    ).compare(left.getGameProfile().getName(), right.getGameProfile().getName()).result();
                }
                return 0;
            }
            return -1;
        }
    };

    public static List<NetworkPlayerInfo> fetchTabList()
    {
        if (KyroClient.mc.thePlayer == null) return new ArrayList<>();
        return playerInfoOrdering.sortedCopy(KyroClient.mc.thePlayer.sendQueue.getPlayerInfoMap());
    }

    public static NBTTagCompound getExtraAttributes(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The item cannot be null!");
        }
        if (!item.hasTagCompound()) {
            return null;
        }

        return item.getSubCompound("ExtraAttributes", false);
    }

    public static boolean isTerminal(final String name) {
        return name.contains("Correct all the panes!") || name.contains("Navigate the maze!") || name.contains("Click in order!") || name.contains("What starts with:") || name.contains("Select all the") || name.contains("Change all to same color!") || name.contains("Click the button on time!");
    }


}
