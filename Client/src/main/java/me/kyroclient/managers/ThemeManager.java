package me.kyroclient.managers;

import me.kyroclient.themes.Themes;

import java.awt.*;

public class ThemeManager {
    public Themes activeTheme = Themes.VAPE;
    public ThemeManager()
    {

    }

    public void setTheme(Themes theme)
    {
        activeTheme = theme;
    }

    public Themes getTheme()
    {
        return activeTheme;
    }

    public Color getPrimaryColor()
    {
        return activeTheme.getPrimary();
    }

    public Color getSecondaryColor()
    {
        return activeTheme.getSecondary();
    }
}
