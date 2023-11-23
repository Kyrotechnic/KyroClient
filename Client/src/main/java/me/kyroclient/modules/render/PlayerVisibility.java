package me.kyroclient.modules.render;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.modules.combat.AntiBot;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Overwrite;

public class PlayerVisibility extends Module {
    public NumberSetting renderRange = new NumberSetting("No Render Within", 3, 1, 15, 1);
    public PlayerVisibility()
    {
        super("Player Visibility", Category.RENDER);

        addSettings(
                renderRange
        );
    }

    @Override
    public void assign()
    {
        KyroClient.playerVisibility = this;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void render(RenderPlayerEvent.Pre event)
    {
        if (!isToggled()) return;

        EntityPlayer player = event.entityPlayer;

        if (player == KyroClient.mc.thePlayer) return;

        if (AntiBot.isValidEntity(player) && player.getDistanceToEntity(KyroClient.mc.thePlayer) < renderRange.getValue())
        {
            event.setCanceled(true);
        }
    }
}
