import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    //alias(libs.plugins.kotlinCocoapods)
    kotlin("plugin.serialization") version "2.1.21"
    //id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

//for MavenLocal set up
//group = "test"
//version = "1.0"
//
//publishing {
//    publications {
//        withType<MavenPublication> {
//            artifactId = "mylibrary"
//        }
//    }
//}


kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release", "debug")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {

            //error handling
//            implementation(kotlin("test"))
//            implementation(kotlin("test-junit"))

            api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

            //IMPLEMENTATIONS ARE INTERNAL ONLY
            //THE KEYWORD API MAKES THEM VISIBLE TO OTHERS
            //commonLib for cryptography
            implementation("dev.whyoleg.cryptography:cryptography-core:0.4.0")
            //authorization for ktor
            implementation("io.ktor:ktor-client-auth:2.3.0")
            //network engine(may or may not be needed)
            implementation("io.ktor:ktor-client-cio:2.3.0")
            //network(ktor) dependencies
            implementation("io.ktor:ktor-client-core:2.3.0")
            implementation("io.ktor:ktor-client-json:2.3.0")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
            //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        }
        iosMain.dependencies {
            //network client
            implementation("io.ktor:ktor-client-darwin:2.3.0")
            //cryptography
            implementation("dev.whyoleg.cryptography:cryptography-provider-apple:0.4.0")
        }
        androidMain.dependencies {
            //network client
            implementation("io.ktor:ktor-client-okhttp:2.3.0")
            //cryptography
            implementation("dev.whyoleg.cryptography:cryptography-provider-jdk:0.4.0")
        }
    }
}

android {
    namespace = "io.github.mamoonamahmood"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
    coordinates(groupId = "io.github.mamoonamahmood", artifactId = "pintrest-kmp" , version = "1.0.2" )

    pom {
        name = "pintrest-kmp"
        description = "shared library for pintrest conversion tracking"
        inceptionYear = "2025"
        url = "https://github.com/MamoonaMahmood/pintrest-kmp-library"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "MamoonaMahmood"
                name = "Mamoona Mahmood"
                email = "mamoona.mahmood@dubizzlelabs.com"
            }
        }
        scm {
            url = "https://github.com/MamoonaMahmood/pintrest-kmp-library"
        }
    }
}
//tasks.register<Jar>("buildJar") {
//    group = "build"
//    archiveBaseName.set("shared-lib")
//    archiveVersion.set("1.0.20")
//    from(kotlin.targets["jvm"].compilations["main"].output)
//}

