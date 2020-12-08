plugins {
    java
    kotlin("jvm") version "1.4.20"
    kotlin("kapt") version "1.4.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
//    maven { url = "https://dl.bintray.com/arrow-kt/arrow-kt/" }
//    maven { url 'https://oss.jfrog.org/artifactory/oss-snapshot-local/' } // for SNAPSHOT builds
}

dependencies {
    implementation(kotlin("stdlib"))
    testCompile("junit", "junit", "4.12")
    "0.11.0".let {
        implementation("io.arrow-kt", "arrow-core", it)
        implementation("io.arrow-kt", "arrow-syntax", it)
        kapt("io.arrow-kt", "arrow-meta", it)
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
