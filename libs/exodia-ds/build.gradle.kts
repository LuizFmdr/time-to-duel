plugins {
    alias(libs.plugins.timetoduel.android.library)
    alias(libs.plugins.timetoduel.android.library.compose)
}

android {
    namespace = "br.com.timetoduel.libs.exodia_ds"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.runtime)

    implementation(libs.androidx.core.ktx)
}
