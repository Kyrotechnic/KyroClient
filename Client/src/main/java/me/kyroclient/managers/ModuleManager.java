package me.kyroclient.managers;

import me.kyroclient.modules.Module;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public String classPath;

    public ModuleManager(String classPath)
    {
        this.classPath = classPath;
    }

    public void initReflection()
    {
        Reflections reflections = new Reflections(classPath);
        Set<Class<? extends Module>> moduleClasses = reflections.getSubTypesOf(Module.class);

        for (Class<? extends Module> clazz : moduleClasses)
        {
            try {
                Module module = clazz.getDeclaredConstructor().newInstance();
                modules.add(module);
                module.assign();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


    }
    public List<Module> getModules()
    {
        return modules;
    }
}
