package me.kyroclient.ui.modern.comps;

import java.awt.Color;

import me.kyroclient.KyroClient;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.ui.modern.ModernClickGui;
import me.kyroclient.ui.modern.comps.Comp;
import me.kyroclient.util.AnimationUtils;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.GLUtil;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;

public class CompBoolSetting
        extends Comp {
    private final BooleanSetting booleanSetting;

    public CompBoolSetting(double x, double y, BooleanSetting tickSetting) {
        this.x = x;
        this.y = y;
        this.booleanSetting = tickSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        int insideC = this.booleanSetting.isEnabled() ? KyroClient.themeManager.getSecondaryColor().getRGB() : KyroClient.themeManager.getPrimaryColor().getRGB();
        RenderUtils.drawBorderedRoundedRect((float) (ModernClickGui.getX() + this.x), (float) (ModernClickGui.getY() + this.y), 10.0f, 10.0f, 3, 1, insideC, KyroClient.themeManager.getSecondaryColor().getRGB());

        if (booleanSetting.isEnabled())
            Fonts.icon.drawString("D", ModernClickGui.getX() + x, (ModernClickGui.getY() + y + 3), Color.WHITE.getRGB());

        Fonts.getPrimary().drawString(this.booleanSetting.name, ModernClickGui.getX() + this.x + 15.0, ModernClickGui.getY() + this.y + 2.0, Color.WHITE.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovered(mouseX, mouseY, ModernClickGui.getX() + this.x, ModernClickGui.getY() + this.y, 10.0, 10.0) && mouseButton == 0) {
            this.booleanSetting.toggle();
        }
    }
}
