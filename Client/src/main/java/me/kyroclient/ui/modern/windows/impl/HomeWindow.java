package me.kyroclient.ui.modern.windows.impl;

import java.awt.Color;
import me.kyroclient.KyroClient;
import me.kyroclient.ui.modern.ModernClickGui;
import me.kyroclient.ui.modern.windows.Window;
import me.kyroclient.util.AnimationUtils;
import me.kyroclient.util.MouseUtils;
import me.kyroclient.util.font.Fonts;

public class HomeWindow extends Window {
    public static AnimationUtils scroll = new AnimationUtils(0.0);
    public int scrollY;
    public HomeWindow() {
        super("Home");
    }

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yOffset = (int) (50 + scroll.getValue());
        for (String str : KyroClient.changelog) {
            Fonts.getPrimary().drawString(str, ModernClickGui.getX() + 100.0, ModernClickGui.getY() + (double)yOffset, Color.WHITE.getRGB());
            yOffset += 12;
        }

        MouseUtils.Scroll scrol = MouseUtils.scroll();

        if (scroll != null)
        {
            switch (scrol)
            {
                case DOWN:
                    if ((scrollY > (ModernClickGui.getHeight() - 25) - getHeight()))
                    {
                        scrollY -= 10;
                    }
                    break;
                case UP:
                    scrollY += 10;
                    if (scrollY >= 0)
                    {
                        scrollY = 0;
                    }

                    if (getHeight() < (ModernClickGui.getHeight() - 25))
                        scrollY = 0;
            }
        }

        scroll.setAnimation(scrollY, 12);
    }

    public int getHeight()
    {
        return 12 * KyroClient.changelog.size();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }
}

