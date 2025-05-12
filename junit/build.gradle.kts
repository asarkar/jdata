plugins {
    id("java-conventions")
    id("java-publish")
}

dependencies {
    implementation(libs.jackson.databind)
    implementation(libs.junit.params)
}
