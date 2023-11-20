package me.kyroclient.util;

public class TypeUtils {
    public static boolean isnof(Object o1, Object o2)
    {
        return !isof(o1, o2);
    }
    public static boolean isof(Object o1, Object o2)
    {
        return is(o1.getClass(), o2.getClass());
    }
    private static boolean is(Class c1, Class c2)
    {
        return c1 == c2;
    }
    public static boolean objClass(Object o1, Class c1)
    {
        return isof(o1, c1);
    }
}
