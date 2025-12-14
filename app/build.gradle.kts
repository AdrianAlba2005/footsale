plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.footsale"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.footsale"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lint {
        // Desactiva la comprobación de opciones obsoletas en lint
        disable += "ObsoleteLintCustomCheck" 
        // O ignora específicamente estos warnings si vienen del compilador de Java
        // (Aunque para warnings de compilación como source/target, la configuración
        // está arriba en compileOptions, y están correctas para Java 8)
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)

    // Dependencias de red
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.logging.interceptor)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging") // NUEVA DEPENDENCIa

    // Dependencias de navegación
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Dependencias de utilidades
    implementation(libs.glide)
    implementation(libs.circleimageview)
    implementation(libs.jwtdecode)
    implementation(libs.contentpager)

    // Shimmer Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Country Code Picker
    implementation("com.hbb20:ccp:2.7.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
