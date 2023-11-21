package me.kyroclient.forge;

import com.google.common.reflect.TypeToken;
import me.kyroclient.util.MathUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    public static Method REGISTER_METHOD;

    public static void register(Object object, boolean randomContainer)
    {
        ModContainer container = getRandomContainer(containers);

        try
        {
            REGISTER_METHOD = EventBus.class.getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
        }
        catch (Exception e)
        {
            REGISTER_METHOD = null;
        }

        REGISTER_METHOD.setAccessible(true);

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

                    try {
                        REGISTER_METHOD.invoke(event, object, method, container);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            catch (NoSuchMethodError e)
            {
                e.printStackTrace();
            }
        }
    }

    public static ModContainer getRandomContainer(List<ModContainer> containers)
    {
        return containers.get(MathUtil.getRandomInRange(containers.size()));
    }

}
