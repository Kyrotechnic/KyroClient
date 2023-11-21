package me.kyroclient.mixins.minecraftforge;

import me.kyroclient.KyroClient;
import me.kyroclient.forge.ForgeSpoofer;
import me.kyroclient.modules.Module;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.reflections.Reflections;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

@Mixin(EventBus.class)
public abstract class MixinEventBus {
    @Shadow protected abstract void register(Class<?> eventType, Object target, Method method, ModContainer owner);
    public boolean hasRegistered = false;
    @Inject(method = "register(Ljava/lang/Object;)V", at = @At("TAIL"), cancellable = false, remap = false)
    public void register(Object object, CallbackInfo ci) throws NoSuchMethodException {
        if (hasRegistered) return;

        hasRegistered = true;

        KyroClient.registerEvents();

        Object obj = KyroClient.eventManager;

        Reflections reflections = new Reflections("net.minecraftforge");

        Set<Class<? extends Event>> moduleClasses = reflections.getSubTypesOf(Event.class);

        Method method = obj.getClass().getDeclaredMethod("handler", Event.class);

        for (Class<? extends Event> eventClass : moduleClasses)
        {
            register(eventClass.getClass(), obj, method, ForgeSpoofer.getRandomContainer(ForgeSpoofer.containers));
        }
    }
}
