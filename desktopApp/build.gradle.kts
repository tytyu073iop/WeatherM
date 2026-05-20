import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "com.example.weatherm.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "WeatherM"
            
            val rawVersion = project.findProperty("appVersionName")?.toString() ?: "1.0.0"
            // Package versions must be numeric for MSI, DEB and DMG.
            packageVersion = run {
                val cleanVersion = rawVersion.removePrefix("v")
                when {
                    cleanVersion.startsWith("nightly-") -> "0.0." + cleanVersion.removePrefix("nightly-")
                    cleanVersion.all { it.isDigit() || it == '.' } && cleanVersion.contains('.') -> cleanVersion
                    else -> "0.0.0" // Fallback for branch names like "master" or "main"
                }
            }
        }
    }
}