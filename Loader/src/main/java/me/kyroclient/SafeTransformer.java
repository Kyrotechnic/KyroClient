package me.kyroclient;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public abstract class SafeTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return this.transform(loader, className, classfileBuffer);
    }

    public abstract byte[] transform(ClassLoader loader, String className, byte[] originalClass);
}
