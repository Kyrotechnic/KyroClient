package me.kyroclient.mixins.minecraftforge;

import me.kyroclient.KyroClient;
import me.kyroclient.forge.ForgeRegister;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.List;

@Mixin(EventBus.class)
public abstract class MixinEventBus {
    @Shadow protected abstract void register(Class<?> eventType, Object target, Method method, ModContainer owner);

    public boolean hasRegistered = false;
    @Inject(method = "register(Ljava/lang/Object;)V", at = @At("TAIL"), cancellable = false, remap = false)
    public void register(Object object, CallbackInfo ci)
    {
        if (hasRegistered) return;

        List<ForgeRegister> register = KyroClient.registerEvents();

        hasRegistered = true;

        for (ForgeRegister reg : register)
        {
            register(reg.clazz, reg.target, reg.method, reg.modContainer);
        }
    }
}
