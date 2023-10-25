package me.kyroclient.themes;

import java.awt.*;

public enum Themes {
    VAPE("Vape", new Color(4049313), new Color(0xFF35B791, true)),
    DEVIL("Devil", new Color(210, 39, 48), new Color(79, 13, 26)),
    PURPLE("Purple", new Color(0xFF7F00FF), new Color(0xFFE100FF)),
    BLACK("Black", new Color(0x0), new Color(0x0)),
    MINT("Mint", new Color(5, 135, 65), new Color(158, 227, 191));
    String name;
    Color mainColor;
    Color accentColor;

    Themes(String name, Color color1, Color color2)
    {
        mainColor = color1;
        accentColor = color2;
        this.name = name;
    }

    public Color getPrimary()
    {
        return mainColor;
    }
    public Color getSecondary()
    {
        return accentColor;
    }
}
