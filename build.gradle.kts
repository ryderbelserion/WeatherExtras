plugins {
    // The shadow jar plugin, It allows you to shade dependencies and create a fat jar, it supports userdev
    // https://github.com/Goooler/shadow
    id("io.github.goooler.shadow") version "8.1.7"

    // The userdev plugin from Paper
    // https://github.com/PaperMC/paperweight
    id("io.papermc.paperweight.userdev") version "1.7.1"

    // The run-paper plugin by jpenilla
    // https://github.com/jpenilla/run-task
    id("xyz.jpenilla.run-paper") version "2.3.0"

    `java-library`
}

// This grabs "1.21" from gradle.properties
val mcVersion = providers.gradleProperty("mcVersion").get()

repositories {
    // This is required for paperweight to function
    maven("https://repo.papermc.io/repository/maven-public")
}

dependencies {
    // The paper equivalent of spigot BuildTools where you get access to the server's net.minecraft.server
    // If an update comes out, and you notice there something you want, refresh your gradle cache
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")
}

// If you want kotlin, You should comment/ out or remove this
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// This will tell paper that we are a plugin that uses spigot mappings
paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(mcVersion)
    }

    compileJava {
        // This makes sure everything uses UTF_8 encoding
        options.encoding = Charsets.UTF_8.name()

        // Sets the release for compiling java
        options.release.set(21)
    }

    // This is used when generating javadocs for your plugin
    javadoc {
        // This makes sure everything uses UTF_8 encoding
        options.encoding = Charsets.UTF_8.name()
    }

    // This is used for applying placeholders to your plugin.yml like below on line 115
    processResources {
        // This makes sure everything uses UTF_8 encoding
        filteringCharset = Charsets.UTF_8.name()
    }

    assemble {
        dependsOn(reobfJar)

        doLast {
            copy {
                from(reobfJar.get())
                into(rootProject.projectDir.resolve("jars"))
            }
        }
    }

    // This will relocate any dependencies that need to be relocated
    // Uncomment this if you need to use it
    /*shadowJar {
        listOf(
            // A common usage is to relocate bstats
            "org.bstats"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }*/

    processResources {
        val props = mapOf(
            // Pulls the name of the project from settings.gradle.kts
            "name" to rootProject.name,
            // Pulls the version of the project which is defined in gradle.properties
            "version" to rootProject.version,
            // Pulls the group of the project which is defined in gradle.properties
            "group" to rootProject.group,
            // Pulls the description of the project which is defined in gradle.properties
            "description" to rootProject.description,
            // Pulls the api version of the project which is defined in gradle.properties
            // apiVersion defines what versions of Paper your plugin will boot on
            // If you wish to support multiple versions, look for "apiVersion" in gradle.properties in the root folder
            // and change it to 1.13 then you should be able to boot up
            "apiVersion" to mcVersion,
            // Pulls the authors of the project which is defined in gradle.properties
            "authors" to providers.gradleProperty("authors").get(),
            // Pulls your website whether it be your github or your actual website defined in gradle.properties
            "website" to providers.gradleProperty("website").get()
        )

        // Maps all the options to your plugin.yml on compilation, it replaces the variables with the correct data
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
