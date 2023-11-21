package me.kyroclient.managers;

import java.util.ArrayList;
import java.util.List;
import me.kyroclient.modules.Module;
import me.kyroclient.ui.modern.windows.Window;
import me.kyroclient.ui.modern.windows.impl.HomeWindow;
import me.kyroclient.ui.modern.windows.impl.ModuleWindow;
import me.kyroclient.ui.modern.windows.impl.ThemeWindow;

public class WindowManager {
    public List<Window> windows = new ArrayList<Window>();

    public WindowManager() {
        this.windows.add(new HomeWindow());
        for (Module.Category category : Module.Category.values()) {
            if (category == Module.Category.CLIENT) continue;
            this.windows.add(new ModuleWindow(category));
        }

        windows.add(new ModuleWindow(Module.Category.CLIENT));

        this.windows.add(new ThemeWindow());
    }

    public Window getDefaultWindow() {
        return this.windows.get(2);
    }
}

