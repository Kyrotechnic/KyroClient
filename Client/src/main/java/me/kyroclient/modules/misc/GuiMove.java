package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PostGuiOpenEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.BooleanSetting;
import me.kyroclient.settings.NumberSetting;
import me.kyroclient.util.SkyblockUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GuiMove extends Module
{
    private BooleanSetting rotate;
    private BooleanSetting drag;
    private BooleanSetting hideTerminalGui;
    private NumberSetting sensivity;
    public static KeyBinding[] binds;

    public GuiMove() {
        super("InvMove", Category.MISC);
        this.rotate = new BooleanSetting("Rotate", true);
        this.drag = new BooleanSetting("Alt drag", true) {
            @Override
            public boolean isHidden() {
                return !GuiMove.this.rotate.isEnabled();
            }
        };
        this.hideTerminalGui = new BooleanSetting("Hide terminals", false);
        this.sensivity = new NumberSetting("Sensivity", 1.5, 0.1, 3.0, 0.01, aBoolean -> !this.rotate.isEnabled());
        this.addSettings(this.hideTerminalGui, this.rotate, this.sensivity, this.drag);
    }

    @Override
    public boolean isToggled() {
        return super.isToggled();
    }

    @Override
    public void assign()
    {
        KyroClient.guiMove = this;
    }

    @Override
    public void onDisable() {
        if (KyroClient.mc.currentScreen != null) {
            for (final KeyBinding bind : GuiMove.binds) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), false);
            }
        }
    }

    private void updateBinds()
    {
        binds = new KeyBinding[] { KyroClient.mc.gameSettings.keyBindSneak, KyroClient.mc.gameSettings.keyBindJump, KyroClient.mc.gameSettings.keyBindSprint, KyroClient.mc.gameSettings.keyBindForward, KyroClient.mc.gameSettings.keyBindBack, KyroClient.mc.gameSettings.keyBindLeft, KyroClient.mc.gameSettings.keyBindRight };
    }

    @SubscribeEvent
    public void onGui(final PostGuiOpenEvent event) {
        if (binds == null) updateBinds();
        if (!(event.gui instanceof GuiChat) && this.isToggled()) {
            for (final KeyBinding bind : GuiMove.binds) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), GameSettings.isKeyDown(bind));
            }
        }
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (KyroClient.mc.currentScreen != null && !(KyroClient.mc.currentScreen instanceof GuiChat) && this.isToggled()) {
            if (binds == null) updateBinds();
            for (final KeyBinding bind : GuiMove.binds) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), GameSettings.isKeyDown(bind));
            }
            if ((KyroClient.mc.currentScreen instanceof GuiContainer || KyroClient.mc.currentScreen instanceof ICrafting) && this.rotate.isEnabled()) {
                KyroClient.mc.mouseHelper.mouseXYChange();
                float f = KyroClient.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
                f *= (float)this.sensivity.getValue();
                final float f2 = f * f * f * 8.0f;
                final float f3 = KyroClient.mc.mouseHelper.deltaX * f2;
                final float f4 = KyroClient.mc.mouseHelper.deltaY * f2;
                int i = 1;
                if (KyroClient.mc.gameSettings.invertMouse) {
                    i = -1;
                }
                if (Keyboard.isKeyDown(56) && Mouse.isButtonDown(2) && this.drag.isEnabled()) {
                    Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 6);
                    KyroClient.mc.setIngameNotInFocus();
                    Mouse.setGrabbed(false);
                }
                KyroClient.mc.thePlayer.setAngles(f3, f4 * i);
            }
        }
    }

    public boolean shouldHideGui(final ContainerChest chest) {
        return SkyblockUtils.isTerminal(chest.getLowerChestInventory().getName()) && this.isToggled() && this.hideTerminalGui.isEnabled();
    }
}
