import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    id("pl.allegro.tech.build.axion-release")
}

java {
    withJavadocJar()
    withSourcesJar()
}

val gitHubUsername: String by project
val gitHubRepo = "$gitHubUsername/${rootProject.name}"
val gitHubUrl: String by project
val gitHubDomain: String = URI(gitHubUrl).host
val gitHubRepoUrl = "$gitHubUrl/$gitHubRepo"

val projectGroup: String by project
val projectDescription: String by project
val licenseName: String by project
val licenseUrl: String by project

scmVersion {
    useHighestVersion = true
    tag {
        prefix = ""
    }
}

version = scmVersion.version

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            pom {
                group = projectGroup
                name = "$projectGroup:${project.name}"
                description = projectDescription
                url = gitHubRepoUrl
                licenses {
                    license {
                        name = licenseName
                        url = licenseUrl
                    }
                }
                developers {
                    developer {
                        id = gitHubUsername
                        url = "$gitHubUrl/$gitHubUsername"
                    }
                }
                scm {
                    connection = "scm:git:git://$gitHubDomain/$gitHubRepo.git"
                    developerConnection = "scm:git:ssh://$gitHubDomain:$gitHubRepo.git"
                    url = gitHubRepoUrl
                }
            }
        }
    }
}

val publishUrl: String by project

repositories {
    maven {
        url = uri(publishUrl)
    }
}
