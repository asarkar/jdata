plugins {
    id("java-conventions")
    id("github-publish")
}

dependencies {
    implementation(libs.jackson.databind)
    implementation(libs.junit.params)
}
