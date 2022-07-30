import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "com.eternalcode"
version = "1.1.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://papermc.io/repo/repository/maven-public/")}
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.eternalcode.pl/releases") }
    maven { url = uri("https://maven.enginehub.org/repo") }
}

dependencies {
    // paper lib, spigot api
    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")

    // Kyori Adventure
    implementation("net.kyori:adventure-platform-bukkit:4.1.1")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")

    // LiteCommands
    implementation("dev.rollczi.litecommands:bukkit:2.3.3")

    // cdn configs
    implementation("net.dzikoysk:cdn:1.13.23")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

bukkit {
    main = "com.eternalcode.check.EternalCheck"
    apiVersion = "1.13"
    prefix = "EternalCheck"
    author = "Osnixer"
    name = "EternalCheck"
    description = "A simple plugin for checking suspicious players"
    version = "${project.version}"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}


tasks.withType<ShadowJar> {
    archiveFileName.set("EternalCheck v${project.version}.jar")

    exclude("org/intellij/lang/annotations/**")
    exclude("org/jetbrains/annotations/**")
    exclude("org/checkerframework/**")
    exclude("META-INF/**")
    exclude("javax/**")

    mergeServiceFiles()
    minimize()

    relocate("panda", "com.eternalcode.check.libs.org.panda")
    relocate("org.panda_lang", "com.eternalcode.check.libs.org.panda")
    relocate("net.dzikoysk", "com.eternalcode.check.libs.net.dzikoysk")
    relocate("dev.rollczi", "com.eternalcode.check.libs.dev.rollczi")
    relocate("org.bstats", "com.eternalcode.check.libs.org.bstats")
    relocate("net.kyori", "com.eternalcode.check.libs.net.kyori")

}