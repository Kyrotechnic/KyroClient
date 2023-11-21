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

                    registerForge(new ForgeRegister(event, object, method, container));
                }

            }
            catch (NoSuchMethodError e)
            {
                e.printStackTrace();
            }
        }
    }

    public static ModContainer getRandomContainer(List<ModContainer> container)
    {
        update();

        return containers.get(MathUtil.getRandomInRange(containers.size()));
    }


    public static void registerForge(ForgeRegister register)
    {
        try
        {
            Constructor<?> ctr = register.clazz.getConstructor();
            ctr.setAccessible(true);
            Event event = (Event)ctr.newInstance();

            Field bid = EventBus.class.getField("busID");
            bid.setAccessible(true);
            int busID = (int) bid.get(MinecraftForge.EVENT_BUS);

            ASMEventHandler listener = new ASMEventHandler(register.target, register.method, register.modContainer);
            event.getListenerList().register(busID, listener.getPriority(), listener);

            Field field = EventBus.class.getField("listeners");
            field.setAccessible(true);
            ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) field.get(MinecraftForge.EVENT_BUS);

            ArrayList<IEventListener> others = listeners.get(register.target);
            if (others == null)
            {
                others = new ArrayList<IEventListener>();
                listeners.put(register.target, others);
            }
            others.add(listener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
