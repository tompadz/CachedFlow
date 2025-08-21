plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    `maven-publish`
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.serialization)
    api(project(":cached_flow"))
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("CachedFlowExtSerialization") {
                groupId = "com.dapadz"
                artifactId = "cachedflow-ext-serialization"
                version = "1.0.0"
                from(components["java"])
                artifact(tasks.named("extSerializationSourcesJar"))
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/dapadz/cachedflow")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
tasks.register<Jar>("extSerializationSourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}