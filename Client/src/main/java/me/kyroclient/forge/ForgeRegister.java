package me.kyroclient.forge;

import net.minecraftforge.fml.common.ModContainer;

import java.lang.reflect.Method;

public class ForgeRegister {
    public Class<?> clazz;
    public Object target;
    public Method method;
    public ModContainer modContainer;

    public ForgeRegister(Class<?> clasz, Object target, Method method, ModContainer modContainer)
    {
        this.clazz = clasz;
        this.target = target;
        this.method = method;
        this.modContainer = modContainer;
    }
}
