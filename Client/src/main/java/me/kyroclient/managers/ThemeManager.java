package me.kyroclient.managers;

import java.awt.Color;
import java.util.ArrayList;

import me.kyroclient.KyroClient;
import me.kyroclient.themes.Theme;
import me.kyroclient.themes.impl.Astolfo;
import me.kyroclient.themes.impl.ColorShift;
import me.kyroclient.themes.impl.Rainbow;

public class ThemeManager {
    public ArrayList<Theme> themes = new ArrayList<>();
    public Theme activeTheme;

    public void setTheme(Theme theme) {
        this.activeTheme = theme;
    }

    public boolean is(String name)
    {
        return name == activeTheme.name;
    }

    public ThemeManager()
    {
        themes.add(activeTheme = new Theme("Vape", new Color(50, 50, 50), new Color(120, 55, 150)));
        themes.add(new Theme("Mint", new Color(5, 135, 65), new Color(158, 227, 191)));
        themes.add(new Theme("Black", new Color(0x0), new Color(0x0)));
        themes.add(new Theme("Purple", new Color(0xFF7F00FF), new Color(0xFFE100FF)));
        themes.add(new Theme("Devil", new Color(210, 39, 48), new Color(79, 13, 26)));
        themes.add(new Astolfo());
        themes.add(new ColorShift());
        themes.add(new Rainbow());

        String nameSelected = KyroClient.clickGui.colorMode.getSelected();

        for (Theme theme : themes)
        {
            if (theme.name.equals(nameSelected))
                activeTheme = theme;
        }
    }

    public Theme getTheme() {
        return this.activeTheme;
    }

    public Color getPrimaryColor() {
        return this.activeTheme.getPrimary();
    }

    public Color getSecondaryColor() {
        return this.activeTheme.getSecondary();
    }
}