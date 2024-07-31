plugins {
    id("io.github.goooler.shadow") version "8.1.7"

    id("io.papermc.paperweight.userdev") version "1.7.1"

    id("xyz.jpenilla.run-paper") version "2.3.0"

    `java-library`
}

val mcVersion = providers.gradleProperty("mcVersion").get()

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.papermc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/releases")
}

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")

    compileOnly(libs.placeholderapi)

    compileOnly(libs.vital.paper)
}

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(mcVersion)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    assemble {
        dependsOn(shadowJar)

        doLast {
            copy {
                from(shadowJar.get())
                into(rootProject.projectDir.resolve("jars"))
            }
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
    }

    processResources {
        val props = mapOf(
            "name" to rootProject.name,
            "version" to rootProject.version,
            "group" to rootProject.group,
            "description" to rootProject.description,
            "apiVersion" to mcVersion,
            "website" to providers.gradleProperty("website").get()
        )

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}