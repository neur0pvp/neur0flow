plugins {
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

val shadePE: Boolean by rootProject.extra

dependencies {
    // True compileOnly deps
    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.72.Final")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Shaded in or bundled by platform-specific code
    if (shadePE) {
        implementation("com.github.retrooper:packetevents-api:2.8.0-SNAPSHOT")
    } else {
        compileOnly("com.github.retrooper:packetevents-api:2.8.0-SNAPSHOT")
    }

    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.kohsuke:github-api:1.327") {
        exclude(group = "commons-io", module = "commons-io")
        exclude(group = "org.apache.commons", module = "commons-lang3")
    }

    implementation("org.incendo:cloud-core:2.0.0")
    implementation("org.incendo:cloud-minecraft-extras:2.0.0-beta.10")
}

buildConfig {
    buildConfigField("String", "GITHUB_REPO", "\"${project.rootProject.ext["githubRepo"]}\"")
}