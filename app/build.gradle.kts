@file:Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
}

val githubUrl = "https://github.com/AlirezaIvaz/BMICalculator"

android {
    namespace = "ir.alirezaivaz.bmi"
    compileSdk = 33

    defaultConfig {
        applicationId = "ir.alirezaivaz.bmi"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"
        buildConfigField(
            "String",
            "GITHUB_REPO_URL",
            "\"$githubUrl\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += "distributor"
    productFlavors {
        create("github") {
            dimension = "distributor"
            versionNameSuffix = "-GH"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"$githubUrl\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"$githubUrl/issues\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"https://github.com/AlirezaIvaz\""
            )
        }
        create("galaxyStore") {
            dimension = "distributor"
            versionNameSuffix = "-SG"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"https://galaxystore.samsung.com/detail/${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"samsungapps://AppRating/${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"samsungapps://SellerDetail/000000055766\""
            )
        }
        create("cafebazaar") {
            dimension = "distributor"
            versionNameSuffix = "-CB"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"https://cafebazaar.ir/app/${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"bazaar://details?id=${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"bazaar://collection?slug=by_author&aid=alirezaivaz\""
            )
        }
        create("myket") {
            dimension = "distributor"
            versionNameSuffix = "-MK"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"https://myket.ir/app/${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"myket://comment?id=${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"myket://developer/${defaultConfig.applicationId}\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.browser)
    implementation(libs.material)
    implementation(libs.tablericons)
}