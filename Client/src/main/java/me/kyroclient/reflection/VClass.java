package me.kyroclient.reflection;

import me.kyroclient.reflection.exceptions.NoMatchingConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class VClass {
    public boolean instanced;
    public Class localClass;
    public Object instance;
    public VClass(Class clasz, boolean instanced)
    {
        this.instanced = instanced;
        this.localClass = clasz;
    }

    public VClass(Class clasz)
    {
        this(clasz,false);
    }

    public VClass instanciate(Object... params) throws NoMatchingConstructorException {
        try {
            Class[] pClasses = this.getTypesFromObjects(params);

            Constructor constructor = localClass.getConstructor(pClasses);

            instance = constructor.newInstance(params);

            instanced = true;
        }
        catch (NoSuchMethodException e)
        {
            throw new NoMatchingConstructorException();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return this;
    }

    public VMethod getMethod(String methodName, boolean staticMethod, Object... paramTypes) throws Exception {
        Method method = localClass.getMethod(methodName, getTypesFromObjects(paramTypes));

        method.setAccessible(true);

        if (!instanced && !staticMethod)
        {
            throw new Exception("Invalid method type!");
        }

        return new VMethod(method);
    }

    //Class utils
    private Class[] getTypesFromObjects(Object... params)
    {
        Class[] pClasses = new Class[params.length];

        int counter = 0;

        for (Object object : params)
        {
            pClasses[counter] = object.getClass();
            counter++;
        }

        return pClasses;
    }
}
