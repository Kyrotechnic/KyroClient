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

    public static final String FONT_LOCALE_LOCATION = KyroClient.mc.mcDataDir + "/config/KyroClient/fonts";
    public static final String FONT_ONLINE_LOCATION = ""

    private static Font getFont(final String location, final int size) {
        Font font;
        try {
            if (Fonts.fontCache.containsKey(location)) {
                font = Fonts.fontCache.get(location).deriveFont(0, (float)size);
            }
            else {
                //final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("kyroclient", "fonts/" + location)).getInputStream();
                InputStream is;

                is = getStream(location);

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

    public static InputStream getStream(String fontName)
    {

    }

    public static void bootstrap() {
        Fonts.tenacity = new MinecraftFontRenderer(getFont("tenacity.ttf", 19), true, false);
        Fonts.tenacityBold = new MinecraftFontRenderer(getFont("tenacity-bold.ttf", 19), true, false);
    }

    static {
        Fonts.fontCache = new HashMap<String, Font>();
    }
}
