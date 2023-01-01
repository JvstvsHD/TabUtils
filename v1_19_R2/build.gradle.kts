plugins {
    java
    id("io.papermc.paperweight.userdev")
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}

group = "de.jvstvshd"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation(project(":common"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

tasks {
    val copyServerJar = task<Copy>("copyServerJar") {
        from(reobfJar)
        rename { "${rootProject.name}-$it" }
        into(
            project.findProperty("pluginsDirectory")?.toString() ?: projectDir.toPath().resolve("build/libs")
                .toString()
        )
    }
    assemble {
        finalizedBy(reobfJar)
    }
    reobfJar {
        finalizedBy(copyServerJar)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

bukkit {
    name = rootProject.name
    version = project.version.toString()
    main = "de.jvstvshd.tabutils.v1_19_R2.TabUtilsPlugin"
    author = "JvstvsHD"
    website = "https://jvstvshd.de"
    description = project.description
    apiVersion = "1.19"
    commands {
        register("tabutils") {
            description = "TabUtils command"
            usage = "/tabutils"
            permission = "tabutils.command"
            permissionMessage = "§cYou don't have the permission to execute this command!"
        }
    }
}