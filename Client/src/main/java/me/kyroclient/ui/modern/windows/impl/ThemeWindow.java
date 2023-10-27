package me.kyroclient.ui.modern.windows.impl;

import me.kyroclient.KyroClient;
import me.kyroclient.managers.ThemeManager;
import me.kyroclient.ui.modern.comps.CompModeSetting;
import me.kyroclient.ui.modern.windows.Window;

public class ThemeWindow
        extends Window {
    public ThemeWindow() {
        super("Themes");
    }

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CompModeSetting modeSetting = new CompModeSetting(95, 30, KyroClient.clickGui.colorMode);
        modeSetting.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        CompModeSetting modeSetting = new CompModeSetting(95, 30, KyroClient.clickGui.colorMode);
        modeSetting.mouseClicked(mouseX, mouseY, mouseButton);

        KyroClient.themeManager.setTheme(KyroClient.clickGui.colorMode.getSelected());
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }
}
