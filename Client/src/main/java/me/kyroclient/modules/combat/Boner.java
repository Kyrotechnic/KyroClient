package me.kyroclient.modules.combat;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.PacketUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Boner extends Module {
    public NumberSetting tickDelay = new NumberSetting("Tick Delay", 3, 1, 5, 1);
    public BooleanSetting swap = new BooleanSetting("Sword Swap", false);
    public BooleanSetting disable = new BooleanSetting("Disable", false);
    public NumberSetting slot = new NumberSetting("Slot To Swap", 6, 1, 9, 1, aBoolean -> !swap.isEnabled());
    int ticks = 0;
    public Boner()
    {
        super("Boner", Category.COMBAT);

        addSettings(
                tickDelay,
                slot,
                swap,
                disable
        );
    }

    @Override
    public void onEnable()
    {
        ticks = 0;
    }

    @Override
    public void onDisable()
    {
        ticks = 0;
        PacketUtils.sendPacket(new C09PacketHeldItemChange(KyroClient.mc.thePlayer.inventory.currentItem));
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;

        ticks++;

        int desiredTicks = (int) tickDelay.getValue();

        if (ticks == desiredTicks)
        {
            ticks = 0;

            throwBone();
        }
    }

    public void throwBone()
    {
        int slotToSwap = (int) (swap.isEnabled() ? slot.getValue() : -1);
        boolean flag = false;

        for (int i = 0; i < 9; i++)
        {
            if (slotToSwap == i) continue;

            ItemStack item = KyroClient.mc.thePlayer.inventory.mainInventory[i];

            if (item == null || item.getItem() != Items.bone) continue;

            flag = true;
            slotToSwap = i;
        }

        if (slotToSwap < 0 || slotToSwap > 8)
        {
            if (disable.isEnabled())
                setToggled(false);
            return;
        }

        if (flag)
        {
            PacketUtils.sendPacket(new C09PacketHeldItemChange(slotToSwap));
            PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(KyroClient.mc.thePlayer.inventory.mainInventory[slotToSwap]));
        }
        else
        {
            KyroClient.mc.thePlayer.inventory.currentItem = slotToSwap - 1;
            if (disable.isEnabled())
                setToggled(false);
        }
    }
}
