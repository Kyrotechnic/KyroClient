package me.kyroclient.modules.combat;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MotionUpdateEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MilliTimer;
import me.kyroclient.util.MovementUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoBlock extends Module
{
    public ModeSetting mode;
    public NumberSetting blockTime;
    public BooleanSetting players;
    public BooleanSetting mobs;
    public BooleanSetting onDamage;
    public BooleanSetting noSlow;
    public MilliTimer blockTimer;
    private boolean isBlocking;

    public AutoBlock() {
        super("AutoBlock", Category.COMBAT);
        this.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel", "Vanilla" });
        this.blockTime = new NumberSetting("Block time", 500.0, 50.0, 2000.0, 50.0);
        this.players = new BooleanSetting("Players", true);
        this.mobs = new BooleanSetting("Mobs", false);
        this.onDamage = new BooleanSetting("on Damage", true);
        this.noSlow = new BooleanSetting("No Slow", false);
        this.blockTimer = new MilliTimer();
        this.addSettings(this.mode, this.blockTime, this.players, this.mobs, this.onDamage, this.noSlow);
    }

    @Override
    public void assign()
    {
        KyroClient.autoBlock = this;
    }

    @SubscribeEvent
    public void onAttacK(final AttackEntityEvent event) {
        if (!this.isToggled() || KyroClient.aura.isToggled()) {
            return;
        }
        if ((event.entityPlayer == KyroClient.mc.thePlayer && ((event.target instanceof EntityPlayer && this.players.isEnabled()) || (!(event.target instanceof EntityPlayer) && this.mobs.isEnabled()))) || (event.target == KyroClient.mc.thePlayer && this.onDamage.isEnabled())) {
            this.blockTimer.reset();
            if (event.entityPlayer == KyroClient.mc.thePlayer && (!MovementUtils.isMoving() || this.mode.is("Vanilla")) && this.isBlocking) {
                this.stopBlocking();
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Post event) {
        if (!this.isToggled() || KyroClient.aura.isToggled()) {
            return;
        }
        if (!this.blockTimer.hasTimePassed((long)this.blockTime.getValue())) {
            if ((!this.isBlocking || this.mode.is("Hypixel")) && this.canBlock()) {
                this.startBlocking();
            }
        }
        else if (this.isBlocking) {
            this.stopBlocking();
        }
    }

    public boolean canBlock() {
        return KyroClient.mc.thePlayer.getHeldItem() != null && KyroClient.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    private void startBlocking() {
        KyroClient.mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C08PacketPlayerBlockPlacement(KyroClient.mc.thePlayer.getHeldItem()));
        this.isBlocking = true;
    }

    private void stopBlocking() {
        if (this.isBlocking) {
            KyroClient.mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.isBlocking = false;
        }
    }

    public boolean isBlocking() {
        return KyroClient.autoBlock.canBlock() && this.isBlocking && !this.noSlow.isEnabled();
    }
}
