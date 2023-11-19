plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.weave-mc.weave-gradle") version "bcf6ab0279"
}

group = "me.kyroclient"
version = ""

minecraft.version("1.8.9")

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val exportLib: Configuration by configurations.creating {
    configurations.compileOnly.get().extendsFrom(this)
}

dependencies {
    shadowImpl("org.reflections:reflections:0.10.2")

    compileOnly("net.minecraft:launchwrapper:1.11")

    compileOnly("org.projectlombok:lombok:1.18.16")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

tasks.withType(Jar::class) {
    manifest.attributes.run {
        this["Premain-Class"] = "me.kyroclient.AgentLoader"
    }
    archiveBaseName.set("loader")
}

tasks.test {
    useJUnitPlatform()
}

tasks.assemble.get().dependsOn(tasks.shadowJar)

tasks.shadowJar
{
    listOf(shadowImpl).forEach {
        println("Copying jars into mod: ${it.files}")
    }
}