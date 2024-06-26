package me.kyroclient.mixins.minecraftforge;

import me.kyroclient.KyroClient;
import me.kyroclient.forge.ForgeRegister;
import me.kyroclient.forge.ForgeSpoofer;
import me.kyroclient.modules.Module;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.reflections.Reflections;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    public void register(Object object, CallbackInfo ci)
    {
        if (hasRegistered) return;

        KyroClient.registerEvents();

        hasRegistered = true;

        for (ForgeRegister reg : ForgeSpoofer.registers)
        {
            register(reg.clazz, reg.target, reg.method, reg.modContainer);
        }
    }
}
