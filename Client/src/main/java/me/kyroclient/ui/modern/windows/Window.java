package me.kyroclient.ui.modern.windows;

public abstract class Window {
    private final String name;
    public static final int LEFT_CLICK = 0;
    public static final int RIGHT_CLICK = 1;

    public Window(String name) {
        this.name = name;
    }

    public abstract void initGui();

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

    public abstract void keyTyped(char typedChar, int keyCode);

    public String getName() {
        return name;
    }

    public boolean isHovered(final int mouseX, final int mouseY, final double x, final double y, final double height, final double width) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
