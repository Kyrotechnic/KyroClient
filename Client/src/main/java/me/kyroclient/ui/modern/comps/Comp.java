package me.kyroclient.ui.modern.comps;

public abstract class Comp {
    public double x;
    public double y;
    public double width;

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public abstract void drawScreen(int mouseX, int mouseY, double scrollY);

    public abstract void keyTyped(char typedChar, int keyCode);

    public boolean isHovered(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (double)mouseX > x && (double)mouseX < x + width && (double)mouseY > y && (double)mouseY < y + height;
    }
}
