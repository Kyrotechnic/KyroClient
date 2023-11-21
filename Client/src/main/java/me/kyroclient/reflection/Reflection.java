package me.kyroclient.reflection;

public class Reflection {
    public static VClass getVirtualClass(Class<?> claszd)
    {
        return new VClass(claszd);
    }
}
