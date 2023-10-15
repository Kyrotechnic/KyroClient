package me.kyroclient.util.font;

import me.kyroclient.KyroClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fonts
{
    private static Map<String, Font> fontCache;
    public static MinecraftFontRenderer iconFont;
    public static MinecraftFontRenderer neverlose;
    public static MinecraftFontRenderer rubik;
    public static MinecraftFontRenderer rubikBold;
    public static MinecraftFontRenderer tahoma;
    public static MinecraftFontRenderer tahomaBold;
    public static MinecraftFontRenderer tenacity;
    public static MinecraftFontRenderer tenacityBold;

    public static MinecraftFontRenderer getPrimary()
    {
        return tenacity;
    }

    public static MinecraftFontRenderer getSecondary()
    {
        return tenacityBold;
    }

    public static MinecraftFontRenderer getIconFont()
    {
        return iconFont;
    }
    private static Font getFont(final String location, final int size) {
        Font font;
        try {
            if (Fonts.fontCache.containsKey(location)) {
                font = Fonts.fontCache.get(location).deriveFont(0, (float)size);
            }
            else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("kyroclient", "fonts/" + location)).getInputStream();
                font = Font.createFont(0, is);
                Fonts.fontCache.put(location, font);
                font = font.deriveFont(0, (float)size);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("arial", 0, size);
        }
        return font;
    }

    public static void bootstrap() {
        Fonts.rubik = new MinecraftFontRenderer(getFont("rubik.ttf", 19), true, false);
        Fonts.rubikBold = new MinecraftFontRenderer(getFont("rubik-bold.ttf", 19), true, false);
        Fonts.tenacity = new MinecraftFontRenderer(getFont("tenacity.ttf", 19), true, false);
        Fonts.tenacityBold = new MinecraftFontRenderer(getFont("tenacity-bold.ttf", 19), true, false);
        Fonts.iconFont = new MinecraftFontRenderer(getFont("icon.ttf", 20), true, false);
        Fonts.tahoma = new MinecraftFontRenderer(getFont("tahoma.ttf", 22), true, false);
        Fonts.tahomaBold = new MinecraftFontRenderer(getFont("tahoma-bold.ttf", 22), true, false);
    }

    static {
        Fonts.fontCache = new HashMap<String, Font>();
    }
}
