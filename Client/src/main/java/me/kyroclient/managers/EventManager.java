package me.kyroclient.managers;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventManager {
    private final Map<Class<?>, List<Consumer<?>>> map = new ConcurrentHashMap<>();

    /**
     * Subscribe an object to the event bus, turning methods defined in it into listeners using @SubscribeEvent.
     *
     * @param obj The object to subscribe.
     * @see SubscribeEvent
     */
    public void subscribe(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class) && method.getParameterCount() == 1) {
                getListeners(method.getParameterTypes()[0]).add(new ReflectEventConsumer(obj, method));
            }
        }
    }

    /**
     * Subscribe a listener to the event bus.
     *
     * @param event   The class of the event to subscribe to.
     * @param handler The Consumer to handle that event.
     */
    public <T extends Event> void subscribe(Class<T> event, Consumer<T> handler) {
        getListeners(event).add(handler);
    }

    /**
     * Call an event for all the listeners listening for it.
     *
     * @param event The event to call.
     */
    public <T extends Event> boolean post(T event) {
        for (Class<?> c = event.getClass(); c != Object.class; c = c.getSuperclass()) {
            //noinspection unchecked
            getListeners(c).forEach(l -> ((Consumer<T>) l).accept(event));
        }

        if (event.isCancelable() && event.isCanceled())
            return true;
        return false;
    }

    /**
     * Unsubscribe a listener from the event bus.
     *
     * @param consumer The Consumer to unsubscribe.
     */
    public void unsubscribe(Consumer<? extends Event> consumer) {
        for (List<Consumer<?>> list : map.values()) {
            list.removeIf(c -> c == consumer);
        }
    }

    /**
     * Unsubscribe an object from the event bus, which unsubscribes all of its listeners.
     *
     * @param obj The object to unsubscribe.
     */
    public void unsubscribe(Object obj) {
        for (List<Consumer<?>> list : map.values()) {
            list.removeIf(c -> c instanceof ReflectEventConsumer && ((ReflectEventConsumer) c).obj == obj);
        }
    }

    /**
     * Returns a list of listeners alongside its event class.
     *
     * @param event The corresponding event class to grab the listeners from.
     * @return a list of listeners corresponding to the event class.
     */
    @NotNull
    private List<Consumer<?>> getListeners(Class<?> event) {
        return map.computeIfAbsent(event, e -> new CopyOnWriteArrayList<>());
    }

    @SubscribeEvent
    public void handler(Event event)
    {
        post(event);
    }

    @AllArgsConstructor
    private class ReflectEventConsumer implements Consumer<Event> {

        private final Object obj;
        private final Method method;

        @Override
        @SneakyThrows
        public void accept(Event event) {
            method.invoke(obj, event);
        }

    }
}
