plugins {
    java
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}

group = "de.jvstvshd"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":common"))
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

tasks {
    val copyServerJar = task<Copy>("copyServerJar") {
        from(shadowJar)
        into(
            project.findProperty("1_8pluginsDirectory")?.toString() ?: projectDir.toPath().resolve("build/libs")
                .toString()
        )
    }
    assemble {
        finalizedBy(shadowJar)
    }
    shadowJar {
        archiveBaseName.set(rootProject.name + "-" + project.name)
        finalizedBy(copyServerJar)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

bukkit {
    name = rootProject.name
    version = project.version.toString()
    main = "de.jvstvshd.tabutils.v1_8_R3.TabUtilsPlugin"
    author = "JvstvsHD"
    website = "https://jvstvshd.de"
    description = project.description
    commands {
        register("tabutils") {
            description = "TabUtils command"
            usage = "/tabutils"
            permission = "tabutils.command"
            permissionMessage = "§cYou don't have the permission to execute this command!"
        }
    }
}