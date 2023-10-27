package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.JoinGameEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.render.FairySoulESP;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.RotationUtils;
import me.kyroclient.util.SkyblockUtils;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class FairySoulAura extends Module {
    public List<EntityArmorStand> clicked = new ArrayList<>();
    public NumberSetting auraRange = new NumberSetting("Range", 3.5, 2, 4, 0.1);
    public FairySoulAura()
    {
        super("Fairy Soul Aura", Category.PLAYER);

        addSettings(auraRange);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (!isToggled() || KyroClient.mc.thePlayer == null || KyroClient.mc.theWorld == null) return;
        List<EntityArmorStand> fairySouls = KyroClient.mc.theWorld.getEntities(EntityArmorStand.class, e -> SkyblockUtils.isFairySoul(e));

        for (EntityArmorStand entity : fairySouls)
        {
            boolean brk = false;
            for (EntityArmorStand entity2 : clicked)
            {
                if (entity == entity2) {
                    brk = true;
                    break;
                }

            }

            if (brk) break;

            if (KyroClient.mc.thePlayer.getPositionEyes(1.0f).distanceTo(entity.getPositionEyes(1.0f)) < auraRange.getValue())
            {
                KyroClient.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, RotationUtils.getLook(entity.getPositionEyes(1.0f))));

                clicked.add(entity);
                break;
            }
        }
    }

    @SubscribeEvent
    public void joinWorld(JoinGameEvent event)
    {
        clicked = new ArrayList<>();
    }
}
