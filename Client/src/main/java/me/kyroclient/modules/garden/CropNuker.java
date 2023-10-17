package me.kyroclient.modules.garden;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.ModeSetting;
import me.kyroclient.settings.NumberSetting;

import java.util.Random;

public class CropNuker extends Module {
    public static String[] MODES = new String[]{"None", "1 Extra", "2 Extra", "3 Extra"};
    public ModeSetting nukerMode = new ModeSetting("Mode", "1 Extra", MODES);
    public NumberSetting extra1 = new NumberSetting("Chance 1", 50, 1, 100, 1, aBoolean -> nukerMode.is("None"));
    public NumberSetting extra2 = new NumberSetting("Chance 2", 40, 1, 100, 1, aBoolean -> !(nukerMode.is("3 Extra") || nukerMode.is("2 Extra")));
    public NumberSetting extra3 = new NumberSetting("Chance 3", 30, 1, 100, 1, aBoolean -> !(nukerMode.is("3 Extra")));
    public BooleanSetting swing = new BooleanSetting("Swing On Nuke", true);
    public BooleanSetting packet = new BooleanSetting("Packet Mine", false);
    public CropNuker()
    {
        super("Crop Nuker", Category.GARDEN);

        addSettings(
            nukerMode,
            extra1,
            extra2,
            extra3,
            swing,
            packet
        );

        random = new Random();
    }

    @Override
    public String suffix()
    {
        return ("" + getAverageBPS()).substring(0, 4) + " BPS";
    }

    public double getAverageBPS()
    {
        switch (nukerMode.getSelected())
        {
            case "1 Extra":
                return 20 + (0.2 * this.extra1.getValue());
            case "2 Extra":
                return 20 + (0.2 * (this.extra1.getValue() + (this.extra2.getValue()*(extra1.getValue()/100))));
            case "3 Extra":
                return 20 + (0.2 * (this.extra1.getValue() + (this.extra2.getValue()*(extra1.getValue()/100)) + (this.extra3.getValue()*((extra1.getValue()+extra2.getValue())/200))));
            case "None":
            default:
                return 20;
        }
    }

    public Random random;

    public int roll()
    {
        int num = 0;
        if (nukerMode.getIndex() > 0)
        {
            int rand = random.nextInt(100);
            if (rand < extra1.getValue())
                num++;
        }

        if (nukerMode.getIndex() > 1 && num == 1)
        {
            int rand = random.nextInt(100);
            if (rand < extra2.getValue())
                num++;
        }

        if (nukerMode.getIndex() > 2 && num == 2)
        {
            int rand = random.nextInt(100);
            if (rand < extra3.getValue())
                num++;
        }

        return num;
    }

    @Override
    public void assign()
    {
        KyroClient.cropNuker = this;
    }
}
