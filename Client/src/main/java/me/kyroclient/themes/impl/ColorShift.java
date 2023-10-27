package me.kyroclient.themes.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.themes.Theme;

import java.awt.*;

public class ColorShift extends Theme {
    public ColorShift()
    {
        super("Color Shift");
    }

    @Override
    public Color getSecondary()
    {
        final float location = (float)((Math.cos((System.currentTimeMillis() * KyroClient.clickGui.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
        if (!KyroClient.clickGui.hsb.isEnabled()) {
            return new Color((int)(KyroClient.clickGui.redShift1.getValue() + (KyroClient.clickGui.redShift2.getValue() - KyroClient.clickGui.redShift1.getValue()) * location), (int)(KyroClient.clickGui.greenShift1.getValue() + (KyroClient.clickGui.greenShift2.getValue() - KyroClient.clickGui.greenShift1.getValue()) * location), (int)(KyroClient.clickGui.blueShift1.getValue() + (KyroClient.clickGui.blueShift2.getValue() - KyroClient.clickGui.blueShift1.getValue()) * location));
        }
        final float[] c1 = Color.RGBtoHSB((int)KyroClient.clickGui.redShift1.getValue(), (int)KyroClient.clickGui.greenShift1.getValue(), (int)KyroClient.clickGui.blueShift1.getValue(), null);
        final float[] c2 = Color.RGBtoHSB((int)KyroClient.clickGui.redShift2.getValue(), (int)KyroClient.clickGui.greenShift2.getValue(), (int)KyroClient.clickGui.blueShift2.getValue(), null);
        return Color.getHSBColor(c1[0] + (c2[0] - c1[0]) * location, c1[1] + (c2[1] - c1[1]) * location, c1[2] + (c2[2] - c1[2]) * location);
    }

    @Override
    public Color getSecondary(int index)
    {
        final float location = (float)((Math.cos((index * 100 + System.currentTimeMillis() * KyroClient.clickGui.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
        if (!KyroClient.clickGui.hsb.isEnabled()) {
            return new Color((int)(KyroClient.clickGui.redShift1.getValue() + (KyroClient.clickGui.redShift2.getValue() - KyroClient.clickGui.redShift1.getValue()) * location), (int)(KyroClient.clickGui.greenShift1.getValue() + (KyroClient.clickGui.greenShift2.getValue() - KyroClient.clickGui.greenShift1.getValue()) * location), (int)(KyroClient.clickGui.blueShift1.getValue() + (KyroClient.clickGui.blueShift2.getValue() - KyroClient.clickGui.blueShift1.getValue()) * location));
        }
        final float[] c1 = Color.RGBtoHSB((int)KyroClient.clickGui.redShift1.getValue(), (int)KyroClient.clickGui.greenShift1.getValue(), (int)KyroClient.clickGui.blueShift1.getValue(), null);
        final float[] c2 = Color.RGBtoHSB((int)KyroClient.clickGui.redShift2.getValue(), (int)KyroClient.clickGui.greenShift2.getValue(), (int)KyroClient.clickGui.blueShift2.getValue(), null);
        return Color.getHSBColor(c1[0] + (c2[0] - c1[0]) * location, c1[1] + (c2[1] - c1[1]) * location, c1[2] + (c2[2] - c1[2]) * location);
    }
}
