package me.kyroclient.modules.player;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MoveEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.MilliTimer;
import me.kyroclient.util.MovementUtils;
import me.kyroclient.util.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Flight extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla");
    public static NumberSetting speed = new NumberSetting("Speed", 0.7, 0.5, 5, .1);
    public MilliTimer disablerTimer;
    public MilliTimer autoDisableTimer;
    private boolean isFlying;
    private boolean placed;
    private double distance;
    private int flyingTicks;
    private int stage;
    private int ticks;
    public Flight()
    {
        super("Flight", Category.PLAYER);

        addSettings(
                mode,
                speed
        );
    }

    @SubscribeEvent
    public void onMove(MoveEvent event)
    {
        if (!isToggled()) return;

        switch (mode.getSelected())
        {
            case "Vanilla":
                event.setY(0.0);
                MovementUtils.setMotion(event, Flight.speed.getValue());
                if (KyroClient.mc.gameSettings.keyBindJump.isKeyDown())
                    event.setY(Flight.speed.getValue());
                if (KyroClient.mc.gameSettings.keyBindSneak.isKeyDown())
                    event.setY(Flight.speed.getValue() * -1.0);
                break;
        }
    }

    /*public boolean isFlying() {
        return this.isToggled() && (!Flight.mode.is("Hypixel Slime") || !this.disablerTimer.hasTimePassed((long)Flight.time.getValue()));
    }*/

    static {
        Flight.mode = new ModeSetting("Mode", "Vanilla", new String[] {"Vanilla"});
        Flight.speed = new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1, aBoolean -> Flight.mode.is("Hypixel"));
    }
}
