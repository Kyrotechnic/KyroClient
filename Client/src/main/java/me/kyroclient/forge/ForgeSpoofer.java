package me.kyroclient.forge;

import com.google.common.reflect.TypeToken;
import me.kyroclient.util.MathUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ForgeSpoofer {
    public static List<ModContainer> containers = new ArrayList<>();
    public static void update()
    {
        containers = Loader.instance().getActiveModList();
    }
    public static void register(Object object)
    {
        register(object, true);
    }

    public static List<ForgeRegister> register(Object object, boolean randomContainer)
    {
        ModContainer container = getRandomContainer(containers);

        List<ForgeRegister> registers = new ArrayList<>();

        for (Method method : object.getClass().getMethods())
        {
            try
            {

                if (method.isAnnotationPresent(SubscribeEvent.class))
                {
                    Class<?>[] param = method.getParameterTypes();

                    if (param.length != 1)
                    {
                        throw new IllegalArgumentException(
                                "Method " + method + " has @SubscribeEvent annotation, but requires " + param.length +
                                        " arguments.  Event handler methods must require a single argument."
                        );
                    }

                    Class<?> event = param[0];

                    if (!Event.class.isAssignableFrom(event))
                    {
                        throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + event);
                    }

                    registers.add(new ForgeRegister(event, object, method, container));
                }

            }
            catch (NoSuchMethodError e)
            {
                e.printStackTrace();
            }
        }

        return registers;
    }

    public static ModContainer getRandomContainer(List<ModContainer> containers)
    {
        return containers.get(MathUtil.getRandomInRange(containers.size()));
    }
}
