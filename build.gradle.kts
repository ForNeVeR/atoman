plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.8.0"
  id("org.jetbrains.intellij") version "1.12.0"
}

group = "me.fornever"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

intellij {
  version.set("2022.2.4")
  type.set("IC")
}

dependencies {
  implementation("edu.illinois.library:imageio-xpm:1.0.1")
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  processResources {
    from("pacmacs/sprites/") {
      include("*.xpm")
      include("*.json")
      include("LICENSE.md")
      into("sprites")
    }

    from("pacmacs/maps/") {
      include("*.txt")
      into("maps")
    }
  }
}
