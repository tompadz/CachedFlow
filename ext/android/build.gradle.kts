plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "com.dapadz.cachedflow.cache.android"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    api(project(":cached_flow"))
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("CachedFlow") {
                groupId = "com.dapadz"
                artifactId = "ext.android"
                version = "1.0.0"
                artifact(tasks.named("extAndroidSourcesJar"))
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
tasks.register<Jar>("extAndroidSourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}