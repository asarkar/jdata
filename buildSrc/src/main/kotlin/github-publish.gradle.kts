plugins {
    `java-library`
    `maven-publish`
}

val projectGroup: String by project
val projectVersion: String by project
val projectDescription: String by project
group = projectGroup
version = projectVersion
description = projectDescription

java {
    withJavadocJar()
    withSourcesJar()
}

val licenseName: String by project
val licenseUrl: String by project
val developerName: String by project
val developerEmail: String by project
val gitHubUsername: String? by lazy { System.getenv("GITHUB_REPOSITORY_OWNER") }
val gitHubUrl: String? by lazy { "github.com/${System.getenv("GITHUB_REPOSITORY")}" }

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("${project.group}:${project.name}")
                description.set(project.description)
                url.set("https://$gitHubUrl")
                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                    }
                }
                developers {
                    developer {
                        name.set(developerName)
                        email.set(developerEmail)
                    }
                }
                scm {
                    connection.set("scm:git:git://$gitHubUrl.git")
                    developerConnection.set("scm:git:ssh://github.com:$gitHubUsername/${project.name}.git")
                    url.set("https://$gitHubUrl")
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
