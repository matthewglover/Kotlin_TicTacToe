import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val MAIN_CLASS_NAME = "game.RunnerKt"

plugins {
  val kotlinVersion = "1.2.51"

  kotlin("jvm") version kotlinVersion
  application
  id("org.jetbrains.kotlin.kapt") version kotlinVersion
}

application {
  mainClassName = MAIN_CLASS_NAME
}

repositories {
  mavenCentral()
}

dependencies {
  val kotlinVersion = "1.2.51"
  val arrowVersion = "0.7.2"

  compile(kotlin("stdlib-jdk8", kotlinVersion))
  compile(kotlin("reflect", kotlinVersion))

  compile("io.arrow-kt:arrow-core:$arrowVersion")
  compile("io.arrow-kt:arrow-syntax:$arrowVersion")
  compile("io.arrow-kt:arrow-typeclasses:$arrowVersion")
  compile("io.arrow-kt:arrow-data:$arrowVersion")
  compile("io.arrow-kt:arrow-instances-core:$arrowVersion")
  compile("io.arrow-kt:arrow-instances-data:$arrowVersion")
  kapt("io.arrow-kt:arrow-annotations-processor:$arrowVersion")
  compile("io.arrow-kt:arrow-effects:$arrowVersion")

  testCompile(kotlin("test", kotlinVersion))
  testCompile("org.jetbrains.spek:spek-api:1.1.5") {
    exclude(group = "org.jetbrains.kotlin")
  }

  testRuntime ("org.jetbrains.spek:spek-junit-platform-engine:1.1.5") {
    exclude(group = "org.junit.platform")
    exclude(group = "org.jetbrains.kotlin")
  }

  testCompile("com.winterbe:expekt:0.5.0")

  testImplementation("io.mockk:mockk:1.8.3")
}

configure<JavaPluginConvention> {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

val fatJar = task("fatJar", type = Jar::class) {
  baseName = "${project.name}-fat"
  manifest { attributes["Main-Class"] = MAIN_CLASS_NAME }
  from(configurations.runtime.map { if (it.isDirectory) it else zipTree(it) })
  with(tasks["jar"] as CopySpec)
}

tasks {
  "build" { dependsOn(fatJar) }
}