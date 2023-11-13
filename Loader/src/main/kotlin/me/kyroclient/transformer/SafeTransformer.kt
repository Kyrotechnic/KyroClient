package me.kyroclient.transformer

import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

internal interface SafeTransformer : ClassFileTransformer {

    fun transform(loader: ClassLoader, className: String, originalClass: ByteArray): ByteArray?

    override fun transform(
        loader: ClassLoader,
        className: String,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classfileBuffer: ByteArray
    ):ByteArray? {
        try {
            return this.transform(loader, className, classfileBuffer)
        } catch (e: Throwable) {
            e.printStackTrace()
            System.exit(1)
        }

        return null;
    }

}