plugins {
    idea
    java
    application
    id("com.github.johnrengelman.shadow") version ("7.0.0")
    id("com.github.ben-manes.versions") version ("0.38.0")
    id("io.freefair.lombok") version ("6.0.0-m2")
    id("com.gorylenko.gradle-git-properties") version ("2.3.1-rc3")
}

group = "fr.rakambda"
description = "YoutubeMP3Updater"

dependencies {
    implementation(libs.slf4j)
    implementation(libs.bundles.log4j2)

    implementation(libs.bundles.raksrinanaUtils)

    implementation(libs.bundles.jackson)

    implementation(libs.unirest)
    implementation(libs.picocli)

    compileOnly(libs.jetbrainsAnnotations)
}

repositories {
    val githubRepoUsername: String by project
    val githubRepoPassword: String by project

    maven {
        url = uri("https://maven.pkg.github.com/Rakambda/JavaUtils/")
        credentials {
            username = githubRepoUsername
            password = githubRepoPassword
        }
    }
    mavenCentral()
}

tasks {
    processResources {
        expand(project.properties)
    }

    compileJava {
        val moduleName: String by project
        inputs.property("moduleName", moduleName)

        options.encoding = "UTF-8"
        options.isDeprecation = true

        doFirst {
            val compilerArgs = options.compilerArgs
            compilerArgs.add("--module-path")
            compilerArgs.add(classpath.asPath)
            classpath = files()
        }
    }

    jar {
        manifest {
            attributes["Multi-Release"] = "true"
        }
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("shaded")
        archiveVersion.set("")
    }

    wrapper {
        val wrapperVersion: String by project
        gradleVersion = wrapperVersion
    }
}

application {
    val moduleName: String by project
    val className: String by project

    mainModule.set(moduleName)
    mainClass.set(className)
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}
