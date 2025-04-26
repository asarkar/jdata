plugins {
    id("java-conventions")
    id("github-publish")
}

dependencies {
    implementation(libs.jackson.databind)
    implementation("org.junit.jupiter:junit-jupiter-params")
}
