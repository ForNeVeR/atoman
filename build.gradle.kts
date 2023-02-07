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

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }
}
