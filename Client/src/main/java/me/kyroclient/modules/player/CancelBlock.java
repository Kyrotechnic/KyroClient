package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.util.PacketUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CancelBlock extends Module {
    public CancelBlock()
    {
        super("Cancel Block", Category.PLAYER);
    }

    @SubscribeEvent
    public void click(PlayerInteractEvent event)
    {
        if (!isToggled() || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;

        ItemStack item = KyroClient.mc.thePlayer.getHeldItem();

        if (item == null || item.getItem() instanceof ItemBow) return;

        event.setCanceled(true);

        PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(item));
    }
}
