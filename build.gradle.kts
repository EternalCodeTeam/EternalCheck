import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "com.eternalcode"
version = "1.1.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.eternalcode.pl/releases") }
    maven { url = uri("https://maven.enginehub.org/repo") }
}

dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    // Kyori Adventure
    implementation("net.kyori:adventure-platform-bukkit:4.3.2")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")

    // LiteCommands
    implementation("dev.rollczi.litecommands:bukkit:2.8.9")

    // CDN
    implementation("net.dzikoysk:cdn:1.14.4")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.2")

    // gitcheck
    implementation("com.eternalcode:gitcheck:1.0.0")
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

tasks {
    runServer {
        minecraftVersion("1.19.3")
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("EternalCheck v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/**",
        "javax/**"
    )

    mergeServiceFiles()
    minimize()

    val prefix = "com.eternalcode.check.libs"

    listOf(
        "panda",
        "org.panda_lang",
        "org.bstats",
        "net.dzikoysk",
        "net.kyori",
        "dev.rollczi"
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }

}