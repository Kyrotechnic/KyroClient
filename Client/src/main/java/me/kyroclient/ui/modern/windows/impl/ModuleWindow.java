package me.kyroclient.ui.modern.windows.impl;

import me.kyroclient.modules.Module;
import me.kyroclient.ui.modern.ModernClickGui;
import me.kyroclient.ui.modern.windows.Window;
import me.kyroclient.util.AnimationUtils;
import me.kyroclient.util.MouseUtils;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.RenderUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleWindow extends Window {
    public static final AnimationUtils scrollAnimation = new AnimationUtils(0.0F);
    public boolean isModuleOpen;
    public static double scrollY;
    public List<Module> modulesInCategory;
    public static Module selectedModule = null;
    public ModuleWindow(Module.Category moduleCategory)
    {
        super(moduleCategory.name);

        isModuleOpen = false;

        modulesInCategory = Module.getModulesByCategory(moduleCategory).stream().sorted(Comparator.comparingDouble(c -> Fonts.getPrimary().getStringWidth(c.getName()))).collect(Collectors.toList());
        Collections.reverse(modulesInCategory);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset = 30;

        if (!ModernClickGui.settingsOpened)
        {
            for (Module module : modulesInCategory)
            {
                RenderUtils.drawBorderedRoundedRect((float) (ModernClickGui.getX() + 95), (float) (ModernClickGui.getY() + offset + scrollAnimation.getValue()), ModernClickGui.getWidth() - 100, 20, 3, 1, module.isToggled() ? ModernClickGui.themeManager.getPrimaryColor().getRGB() : ModernClickGui.themeManager.getSecondaryColor().getRGB(), ModernClickGui.themeManager.getSecondaryColor().getRGB());
                Fonts.getPrimary().drawString(module.getName(), ModernClickGui.getX() + 105, ModernClickGui.getY() + offset + scrollAnimation.getValue() + 8, Color.WHITE.getRGB());
                offset += 25;
            }
        }
        else if (selectedModule != null)
        {

        }

        final MouseUtils.Scroll scroll = MouseUtils.scroll();

        if (scroll != null && !ModernClickGui.settingsOpened) {
            switch (scroll) {
                case DOWN:
                    if (scrollY > -(modulesInCategory.size() - 8) * 25) {
                        scrollY -= 25;
                    }
                    break;
                case UP:
                    if (scrollY < -10) {
                        scrollY += 25;
                    } else {
                        if (modulesInCategory.size() > 8) {
                            scrollY = 0;
                        }
                    }
                    break;
            }
        }

        scrollAnimation.setAnimation(scrollY, 16);
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
