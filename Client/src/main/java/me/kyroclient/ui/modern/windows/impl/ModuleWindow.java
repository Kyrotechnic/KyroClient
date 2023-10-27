package me.kyroclient.ui.modern.windows.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.*;
import me.kyroclient.ui.modern.ModernClickGui;
import me.kyroclient.ui.modern.comps.*;
import me.kyroclient.ui.modern.windows.Window;
import me.kyroclient.util.AnimationUtils;
import me.kyroclient.util.MouseUtils;
import me.kyroclient.util.StencilUtils;
import me.kyroclient.util.font.Fonts;
import me.kyroclient.util.render.RenderUtils;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import static net.minecraft.client.gui.GuiScreen.getClipboardString;

public class ModuleWindow extends Window {
    public AnimationUtils scrollAnimation = new AnimationUtils(0.0);
    public static double scrollY;
    public static double scrollYsettings;
    public List<Module> modulesInCategory;
    public static Module selectedModule;
    public static StringSetting selectedString = null;
    public static NumberSetting selectedNumber = null;
    public static Module changeBind = null;
    public static AnimationUtils settingsAnimation = new AnimationUtils(0.0);

    public ModuleWindow(Module.Category moduleCategory) {
        super(moduleCategory.name);
        this.modulesInCategory = Module.getModulesByCategory(moduleCategory).stream().sorted(Comparator.comparingDouble(c -> Fonts.getPrimary().getStringWidth(c.getName()))).collect(Collectors.toList());
        Collections.reverse(this.modulesInCategory);
    }

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MouseUtils.Scroll scroll;
        int offset = 30;
        if (!ModernClickGui.settingsOpened) {
            for (Module module : this.modulesInCategory) {
                RenderUtils.drawBorderedRoundedRect((float)(ModernClickGui.getX() + 95.0), (float)(ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue()), ModernClickGui.getWidth() - 100.0f, 20.0f, 3.0f, 1.0f, module.isToggled() ? KyroClient.themeManager.getSecondaryColor().getRGB() : KyroClient.themeManager.getPrimaryColor().getRGB(), KyroClient.themeManager.getSecondaryColor().getRGB());
                Fonts.getPrimary().drawString(module.getName(), ModernClickGui.getX() + 105.0, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 6.0, Color.WHITE.getRGB());
                if (!module.getSettings().isEmpty()) {
                    Fonts.icon.drawString("C", ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 21.0, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 8.0, Color.WHITE.getRGB());
                }
                if (changeBind == module)
                {
                    Fonts.getPrimary().drawString("[...]", ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 40, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 8.0, Color.WHITE.getRGB());
                }
                else
                {
                    Fonts.getPrimary().drawString("[" + (char) module.getKeycode() + "]", ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 40, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 8.0, Color.WHITE.getRGB());
                }
                offset += 25;
            }
        } else if (selectedModule != null) {
            Fonts.getPrimary().drawString(selectedModule.getName(), ModernClickGui.getX() + 95.0, 30.0, Color.WHITE.getRGB());

            for (Comp comp : updateComps()) {
                comp.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
        scroll = MouseUtils.scroll();
        if (scroll != null && !ModernClickGui.settingsOpened) {
            switch (scroll) {
                case DOWN: {
                    if (!(scrollY > (double)(-(this.modulesInCategory.size() - 8) * 25))) break;
                    scrollY -= 25.0;
                    break;
                }
                case UP: {
                    if (scrollY < -10.0) {
                        scrollY += 25.0;
                        break;
                    }
                    if (this.modulesInCategory.size() <= 8) break;
                    scrollY = 0.0;
                }
            }
        }
        else if (scroll != null && ModernClickGui.settingsOpened)
        {
            switch (scroll)
            {
                case DOWN: {
                    if ((scrollYsettings > (ModernClickGui.getHeight() - 25.0f) - settingsHeight))
                        scrollYsettings -= 10;
                    break;
                }
                case UP: {
                    scrollYsettings += 10;
                    if (scrollYsettings >= 0)
                    {
                        scrollYsettings = 0;
                    }
                    if (settingsHeight < (ModernClickGui.getHeight() - 25))
                        scrollYsettings = 0.0;
                }
            }
        }
        this.scrollAnimation.setAnimation(scrollY, 16.0);
        settingsAnimation.setAnimation(scrollYsettings, 16);
        StencilUtils.disableStencilBuffer();
    }

    public List<Comp> updateComps() {
        List<Comp> comps = new ArrayList<>();
        int settingOffset = 30 + (int) settingsAnimation.getValue();
        boolean lastBool = false;
        for (Setting s : selectedModule.getSettings()) {
            if (s.isHidden()) continue;
            if (!(s instanceof BooleanSetting) && lastBool) {
                lastBool = false;
                settingOffset += 15;
            }
            if (s instanceof BooleanSetting && !lastBool) {
                comps.add(new CompBoolSetting(95.0, settingOffset, (BooleanSetting)s));
                lastBool = true;
                continue;
            }
            else if (s instanceof BooleanSetting && lastBool)
            {
                comps.add(new CompBoolSetting(95.0f + (ModernClickGui.getWidth() - 95.0f) / 2.0f, settingOffset, (BooleanSetting)s));
                settingOffset += 15;
                lastBool = false;
            }
            else if (s instanceof ModeSetting)
            {
                comps.add(new CompModeSetting(95, settingOffset, (ModeSetting) s));
                settingOffset += 20;
            }
            else if (s instanceof StringSetting)
            {
                comps.add(new CompStringSetting(95, settingOffset, (StringSetting) s));
                settingOffset += 20;
            }
            else if (s instanceof RunnableSetting)
            {
                comps.add(new CompRunnableSetting(95, settingOffset, (RunnableSetting) s));
                settingOffset += 20;
            }
            else if (s instanceof NumberSetting)
            {
                comps.add(new CompSliderSetting(95, settingOffset, (NumberSetting) s));
                settingOffset += 25;
            }

        }
        settingsHeight = settingOffset - (int) (settingsAnimation.getValue());
        return comps;
    }

    public int settingsHeight;
    public void close()
    {
        selectedModule = null;
        settingsHeight = 0;
        scrollYsettings = 0;
        settingsAnimation.setAnimation(0, 0);
        ModernClickGui.settingsOpened = false;
        selectedString = null;
        selectedNumber = null;
        changeBind = null;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int offset = 30;
        changeBind = null;
        if (selectedModule == null) {
            for (Module module : this.modulesInCategory) {
                if (this.isHovered(mouseX, mouseY, ModernClickGui.getX() + 95.0, ModernClickGui.getY() + (double) offset + this.scrollAnimation.getValue(), ModernClickGui.getWidth() - 100.0f, 20.0)) {
                    switch (mouseButton) {
                        case 0: {
                            module.toggle();
                            break;
                        }
                        case 1: {
                            if (module.getSettings().isEmpty()) {
                                return;
                            }
                            close();
                            selectedModule = module;
                            ModernClickGui.settingsOpened = true;
                            break;
                        }
                        case 2: {
                            changeBind = module;
                            break;
                        }
                    }
                }
                offset += 25;
            }
        }
        else
        {
            for (Comp comp : updateComps())
            {
                comp.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        KyroClient.configManager.saveConfig();
        if (selectedModule == null) return;

        for (Comp comp : updateComps())
        {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }

        if (selectedNumber != null)
        {
            selectedNumber = null;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        KyroClient.configManager.saveConfig();
        if (changeBind != null)
        {
            changeBind.setKeycode(keyCode);
        }
        if (selectedModule == null) return;
        if (selectedString == null) return;

        else if (selectedString != null) {
            if (keyCode == 28) {
                selectedString = null;
            }
            else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                selectedString.setValue(this.selectedString.getValue() + getClipboardString());
            }
            else if (keyCode != 14) {
                selectedString.setValue(ChatAllowedCharacters.filterAllowedCharacters(this.selectedString.getValue() + typedChar));
            }
            else {
                selectedString.setValue(this.selectedString.getValue().substring(0, Math.max(0, this.selectedString.getValue().length() - 1)));
            }
        }
    }

    static {
        selectedModule = null;
    }
}
