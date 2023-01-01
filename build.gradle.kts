plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2" apply false
    id("io.papermc.paperweight.userdev") version "1.4.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("org.cadixdev.licenser") version "0.6.1"
}

group = "de.jvstvshd"
version = "1.0.0"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-snapshots/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.9.1")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
}

tasks {
    /*val copyServerJar = task<Copy>("copyServerJar") {
        from(reobfJar)
        into(
            project.findProperty("1_8pluginsDirectory")?.toString() ?: projectDir.toPath().resolve("build/libs")
                .toString()
        )
    }
    reobfJar {
        finalizedBy(copyServerJar)
    }*/
}

license {
    header(rootProject.file("HEADER.txt"))
    include("**/*.java")
    newLine(true)
}

tasks.test {
    useJUnitPlatform()
}

