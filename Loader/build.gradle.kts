plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.trust.loader"
version = ""

repositories {
    mavenCentral()
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