package me.kyroclient.ui.modern.comps;

import me.kyroclient.KyroClient;
import me.kyroclient.settings.RunnableSetting;
import me.kyroclient.ui.modern.ModernClickGui;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.RenderUtils;

import java.awt.*;

public class CompRunnableSetting extends Comp {
    public RunnableSetting runnableSetting;
    public CompRunnableSetting(int x, int y, RunnableSetting runnableSetting)
    {
        this.x = x;
        this.y = y;
        this.runnableSetting = runnableSetting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY)
    {
        RenderUtils.drawBorderedRoundedRect((float) (ModernClickGui.getX() + x), (float) (ModernClickGui.getY() + y), (float) (ModernClickGui.getWidth() - x - 5), 15, 5, 1, KyroClient.themeManager.getPrimaryColor().getRGB(), KyroClient.themeManager.getSecondaryColor().getRGB());

        Fonts.getPrimary().drawCenteredString(runnableSetting.name, (float) (ModernClickGui.getX() + x + (ModernClickGui.getWidth() - x)/2), (float) (ModernClickGui.getY() + y + 3), Color.WHITE.getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
