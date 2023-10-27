package me.kyroclient.themes.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.themes.Theme;

import java.awt.*;

public class Rainbow extends Theme {
    public Rainbow()
    {
        super("Rainbow");
    }

    @Override
    public Color getSecondary()
    {
        return Color.getHSBColor((float) ((System.currentTimeMillis() * KyroClient.clickGui.rgbSpeed.getValue()) / 5000.0 % 1.0), 0.8f, 1.0f);
    }
}
