import net.minecraftforge.gradle.common.util.MinecraftExtension
import org.spongepowered.asm.gradle.plugins.MixinExtension

buildscript {
    repositories {
        maven { url = uri("https://files.minecraftforge.net/maven") }
        maven { url = uri("http://repo.spongepowered.org/maven") }
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:3.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/ephemeral-laboratories/dozenal")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
    maven {
        url = uri("https://www.dogforce-games.com/maven/")
    }
}

plugins {
    java
}
apply(plugin = "net.minecraftforge.gradle")
apply(plugin = "org.spongepowered.mixin")

group = "garden.ephemeral.minecraft"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    // Why is this still not the default, Gradle?!
    options.encoding = "UTF-8"
}

configure<MinecraftExtension> {
    mappings("snapshot", "20200820-1.16.1")

    // Default run configurations.
    runs {
        register("client") {
            workingDirectory(project.file("run"))

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")

            // Mixin doesn't get loaded when running the run config so this is the workaround.
            args("--mixin", "mixins.dozenal.json")

            mods {
                register("dozenal") {
                    source(sourceSets["main"])
                }
            }
        }
    }
}

configurations {
    val shade = create("shade")
    getByName("compile").extendsFrom(shade)
}

configure<MixinExtension> {
    // XXX: Might be unnecessary now because I get a warning about it being a duplicate.
    add(sourceSets["main"], "mixins.dozenal.refmap.json")
}

dependencies {
    "minecraft"("net.minecraftforge:forge:1.16.2-33.0.10")

    "implementation"("org.spongepowered:mixin:0.8")
    "shade"("garden.ephemeral.dozenal:dozenal:1.0.2")

    "testImplementation"("org.junit.jupiter:junit-jupiter:5.6.2")
    "testImplementation"("org.hamcrest:hamcrest:2.2")
}

tasks.named<Jar>("jar") {
    finalizedBy("reobfJar")

    configurations["shade"].forEach { dep ->
        from(project.zipTree(dep)) {
            exclude("META-INF", "META-INF/**")
        }
    }

    manifest {
//        attributes "FMLCorePlugin": mod_core_plugin
//        attributes "FMLCorePluginContainsFMLMod": "true"
//        attributes "ForceLoadAsMod": "true"
        attributes(
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "TweakOrder" to "0",
                "MixinConfigs" to "mixins.dozenal.json"
        )
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}