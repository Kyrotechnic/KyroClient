package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiBan extends Module {
    public static String ip;
    public AntiBan()
    {
        super("Anti Ban", Category.MISC);

        setToggled(true);
    }

    @SubscribeEvent
    public void serverJoin(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            if (KyroClient.mc.getCurrentServerData() == null) return;
            ip = KyroClient.mc.getCurrentServerData().serverIP;
        }
    }

    public static void reconnect()
    {
        System.out.println("Attempting to bypass ban?");
        if (ip != null)
        {
            FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", ip, false));
            KyroClient.banned = true;
        }
    }
}
