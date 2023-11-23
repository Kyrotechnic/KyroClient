package me.kyroclient.capes;

import me.kyroclient.KyroClient;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class CapeBearer {
    public String username;
    public ResourceLocation capeLocation;
    public CapeBearer(String username, String capeUrl)
    {
        this.username = username;

        loadCape(capeUrl);
    }

    public ResourceLocation getCapeLocation()
    {
        return capeLocation;
    }

    public void loadCape(String link)
    {
        try {
            capeLocation = KyroClient.mc.getTextureManager().getDynamicTextureLocation("kyro_" + username, new DynamicTexture(ImageIO.read(new URL(link))));
        } catch (IOException e) {
            capeLocation = null;
            e.printStackTrace();
        }
    }
}
