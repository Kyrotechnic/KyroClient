package me.kyroclient.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public abstract class SafeTransformer implements ClassFileTransformer {
    public abstract byte[] transform(ClassLoader loader, String className, byte[] originalClass);
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return this.transform(loader, className, classfileBuffer);
    }
}
