plugins {
    id("java")
}

group = "me.trust.loader"
version = ""

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
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