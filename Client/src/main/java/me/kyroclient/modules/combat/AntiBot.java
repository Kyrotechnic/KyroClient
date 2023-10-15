package me.kyroclient.modules.combat;

import me.kyroclient.KyroClient;
import me.kyroclient.events.JoinGameEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class AntiBot extends Module
{
    private static final ModeSetting mode;
    private static final BooleanSetting ticksInvis;
    private static final BooleanSetting tabTicks;
    private static final BooleanSetting npcCheck;
    private static final HashMap<Integer, EntityData> entityData;

    public AntiBot() {
        super("Anti Bot", Module.Category.COMBAT);
        this.addSettings(AntiBot.mode, AntiBot.ticksInvis, AntiBot.tabTicks, AntiBot.npcCheck);
    }

    @Override
    public void assign()
    {
        KyroClient.antiBot = this;
    }

    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        final EntityData data = AntiBot.entityData.get(event.entity.getEntityId());
        if (data == null) {
            AntiBot.entityData.put(event.entity.getEntityId(), new EntityData(event.entity));
        }
        else {
            AntiBot.entityData.get(event.entity.getEntityId()).update();
        }
    }

    public static boolean isValidEntity(final Entity entity) {
        if (KyroClient.antiBot.isToggled() && entity instanceof EntityPlayer && entity != KyroClient.mc.thePlayer) {
            final EntityData data = AntiBot.entityData.get(entity.getEntityId());
            if (data != null && AntiBot.mode.is("Hypixel")) {
                return (!AntiBot.tabTicks.isEnabled() || data.getTabTicks() >= 150) && (!AntiBot.ticksInvis.isEnabled() || data.getTicksExisted() - data.getTicksInvisible() >= 150) && (!AntiBot.npcCheck.isEnabled() || !PlayerUtil.isNPC(entity));
            }
        }
        return true;
    }

    @SubscribeEvent
    public void onWorldJOin(final JoinGameEvent event) {
        AntiBot.entityData.clear();
    }

    static {
        mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel" });
        ticksInvis = new BooleanSetting("Invis ticks check", true, aBoolean -> !AntiBot.mode.is("Hypixel"));
        tabTicks = new BooleanSetting("Tab ticks check", false, aBoolean -> !AntiBot.mode.is("Hypixel"));
        npcCheck = new BooleanSetting("NPC check", true, aBoolean -> !AntiBot.mode.is("Hypixel"));
        entityData = new HashMap<Integer, EntityData>();
    }

    private static class EntityData
    {
        private int ticksInvisible;
        private int tabTicks;
        private final Entity entity;

        public EntityData(final Entity entity) {
            this.entity = entity;
            this.update();
        }

        public int getTabTicks() {
            return this.tabTicks;
        }

        public int getTicksInvisible() {
            return this.ticksInvisible;
        }

        public int getTicksExisted() {
            return this.entity.ticksExisted;
        }

        public void update() {
            if (this.entity instanceof EntityPlayer && KyroClient.mc.getNetHandler() != null && KyroClient.mc.getNetHandler().getPlayerInfo(this.entity.getUniqueID()) != null) {
                ++this.tabTicks;
            }
            if (this.entity.isInvisible()) {
                ++this.ticksInvisible;
            }
        }
    }
}
