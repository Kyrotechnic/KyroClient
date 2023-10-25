package me.kyroclient.managers;

import me.kyroclient.modules.Module;
import me.kyroclient.ui.modern.windows.Window;
import me.kyroclient.ui.modern.windows.impl.HomeWindow;
import me.kyroclient.ui.modern.windows.impl.ModuleWindow;
import me.kyroclient.ui.modern.windows.impl.ThemeWindow;

import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    public List<Window> windows = new ArrayList<>();
    public WindowManager()
    {
        windows.add(new HomeWindow());

        for (Module.Category category : Module.Category.values())
        {
            windows.add(new ModuleWindow(category));
        }

        windows.add(new ThemeWindow());
    }

    public Window getDefaultWindow()
    {
        return windows.get(2);
    }
}
