package me.kyroclient.modules.misc;

import com.google.common.collect.Lists;
import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.util.PlayerUtil;
import me.kyroclient.util.Process;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BanInfo extends Module {
    public List<String> lastOnlinePlayers = Lists.newArrayList();
    public HashMap<String, Long> playersLeftList = new HashMap<>();
    public BanInfo()
    {
        super("Ban Info", Category.MISC);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (PlayerUtil.isOnSkyBlock() || KyroClient.mc.thePlayer == null) return;

        for (String str : playersLeftList.keySet())
        {
            long value = playersLeftList.get(str);
            if (System.currentTimeMillis() - value > 2000)
                playersLeftList.remove(str);
        }

        List<String> players = KyroClient.mc.getNetHandler().getPlayerInfoMap().stream().map(c -> c.getGameProfile().getName()).collect(Collectors.toList());
        Iterator<String> it = lastOnlinePlayers.iterator();

        while (it.hasNext())
        {
            String player = it.next();
            if (!player.contains(player))
                playersLeftList.put(player, System.currentTimeMillis());
        }

        lastOnlinePlayers = players;
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event)
    {
        lastOnlinePlayers.clear();
        playersLeftList.clear();
    }

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event)
    {
        if (!PlayerUtil.onHypixel() || !isToggled()) return;

        String message = event.message.getUnformattedText();
        if (message.contains("A player has been removed from") && !message.contains(":"))
        {
            new Process(() -> {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (playersLeftList.isEmpty())
                {
                    KyroClient.sendMessage("§6[KyroClient] §fNo banned players found!");
                    return;
                }

                String players = String.join("§c, §e", playersLeftList.keySet());
                KyroClient.sendMessage("§6[KyroClient] §fPlayers who were possibly banned: §e" + players + "§c!");
            });
        }
    }
}
