plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.runPaper)

    `java-library`
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.papermc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

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

        minecraftVersion(libs.versions.minecraft.get())
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
            "apiVersion" to libs.versions.minecraft.get(),
            "website" to providers.gradleProperty("website").get()
        )

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}