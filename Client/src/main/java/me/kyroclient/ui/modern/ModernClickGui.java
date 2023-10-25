package me.kyroclient.ui.modern;

import me.kyroclient.KyroClient;
import me.kyroclient.managers.ThemeManager;
import me.kyroclient.managers.WindowManager;
import me.kyroclient.modules.Module;
import me.kyroclient.ui.modern.windows.Window;
import me.kyroclient.util.StencilUtils;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.GLUtil;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ModernClickGui extends GuiScreen {
    public WindowManager windowManager;
    public static Window selectedWindow;
    public static ThemeManager themeManager;
    public static boolean settingsOpened = false;
    private static double x = 100;
    private static double y = 100;
    public ModernClickGui()
    {
        windowManager = new WindowManager();
        themeManager = new ThemeManager();
        selectedWindow = windowManager.getDefaultWindow();

    }

    @Override
    public void initGui()
    {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        this.x = ((double) sr.getScaledWidth() / 2) - (this.getWidth() / 2);
        this.y = ((double) sr.getScaledHeight() / 2) - (this.getHeight() / 2);

        for (Window window : windowManager.windows)
        {
            window.initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        int categoryOffset = 35;
        GLUtil.startScale((float) ((this.getX()) + (this.getX() + this.getWidth())) / 2, (float) ((this.getY()) + (this.getY() + this.getHeight())) / 2, 1);

        RenderUtils.drawBorderedRoundedRect((float) this.getX(), (float) this.getY(), 85, this.getHeight(), 3, 2, themeManager.getPrimaryColor().getRGB(), themeManager.getSecondaryColor().getRGB());
        RenderUtils.drawBorderedRoundedRect((float) (this.getX() + 90), (float) this.getY(), this.getWidth() - 90, 20, 3, 2, themeManager.getPrimaryColor().getRGB(), themeManager.getSecondaryColor().getRGB());
        RenderUtils.drawBorderedRoundedRect((float) (this.getX() + 90), (float) (this.getY() + 25), this.getWidth() - 90, this.getHeight() - 25, 3, 2, themeManager.getPrimaryColor().getRGB(), themeManager.getSecondaryColor().getRGB());

        Fonts.getSecondary().drawCenteredString("KyroClient", (float) (this.getX() + (42.5)), (float) (this.getY() + 6), Color.WHITE.getRGB());

        for (Window window : windowManager.windows)
        {
            if (window == selectedWindow)
            {
                RenderUtils.drawBorderedRoundedRect((float) (this.getX() + 5), (float) (this.getY() + categoryOffset), 75, 15, 4, 2, themeManager.getPrimaryColor().brighter().getRGB(), themeManager.getSecondaryColor().getRGB());
            }
            Fonts.getPrimary().drawStringWithShadow(window.getName(), this.getX() + 12, this.getY() + categoryOffset + 5, Color.WHITE.getRGB());

            categoryOffset += 25;
        }

        selectedWindow.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        selectedWindow.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int categoryOffset = 35;

        for (Window c : windowManager.windows) {
            if (isHovered(mouseX, mouseY, this.getX() + 4, this.getY() + categoryOffset, 75, 16) && mouseButton == 0) {
                selectedWindow = c;
                settingsOpened = false;
            }

            categoryOffset += 25;
        }

        selectedWindow.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        selectedWindow.keyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            if (settingsOpened) {
                settingsOpened = false;
            } else {
                mc.displayGuiScreen(null);
            }
        }
    }

    public void onGuiClosed() {
        settingsOpened = false;
        KyroClient.configManager.saveConfig();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static double getX() {
        return x;
    }

    public static double getY() {
        return y;
    }

    public static float getWidth() {
        return 305;
    }

    public static float getHeight() {
        return 230;
    }

    public boolean isHovered(final int mouseX, final int mouseY, final double x, final double y, final double height, final double width) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
