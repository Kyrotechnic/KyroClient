package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.events.MotionUpdateEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.Rotation;
import me.kyroclient.util.RotationUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class YawSetter extends Module {
    public NumberSetting yaw = new NumberSetting("Yaw", 0, -180, 180, 1);
    private Rotation rotation;
    public YawSetter()
    {
        super("Yaw Setter", Category.GARDEN);

        addSettings(
                yaw
        );
    }


    @SubscribeEvent
    public void motion(MotionUpdateEvent.Pre event)
    {
        if (!isToggled() || rotation == null) return;

        event.setRotation(RotationUtils.getSmoothRotation(RotationUtils.getLastReportedRotation(), rotation, 12));

        if (RotationUtils.getLastReportedRotation() == rotation)
            setToggled(false);
    }

    @Override
    public void onEnable()
    {
        rotation = new Rotation((float) yaw.getValue(), KyroClient.mc.thePlayer.rotationPitch);
    }

    @Override
    public void onDisable()
    {
        rotation = null;
    }
}
