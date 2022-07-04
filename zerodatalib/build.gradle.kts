plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.protobuf")
}

android {
    compileSdkVersion(32)

    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(32)
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions("implementation")

    productFlavors {
        create("ui") {
            buildConfigField("String", "IP_STACK_API_KEY", "\"c1aab8424db6187a6d0e5baa164afc13\"")
            buildConfigField("String", "IP_STACK_BASE_URL", "\"http://api.ipstack.com/\"")
            buildConfigField("String", "AWS_IP_BASE_URL", "\"http://checkip.amazonaws.com/\"")
            buildConfigField("String", "API_URL", "\"https://api2.dns.simplifyd.systems/v1/\"")
            buildConfigField("String", "ANDROID_APP_LOGIN", "\"66868\"")
            buildConfigField("String", "ANDROID_APP_PASSWORD", "\"I52nvt29\"")
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-7604868220609576~8057365177\"")
            dimension = "implementation"
            matchingFallbacks = mutableListOf("uiRelease")
        }
        create("skeleton") {
            buildConfigField("String", "IP_STACK_API_KEY", "\"c1aab8424db6187a6d0e5baa164afc13\"")
            buildConfigField("String", "IP_STACK_BASE_URL", "\"http://api.ipstack.com/\"")
            buildConfigField("String", "AWS_IP_BASE_URL", "\"http://checkip.amazonaws.com/\"")
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-7604868220609576~8057365177\"")
            buildConfigField("String", "API_URL", "\"https://api2.dns.simplifyd.systems/v1/\"")
            buildConfigField("String", "ANDROID_APP_LOGIN", "\"66868\"")
            buildConfigField("String", "ANDROID_APP_PASSWORD", "\"I52nvt29\"")
            dimension = "implementation"
            matchingFallbacks = mutableListOf("skeletonRelease")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.4.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("androidx.test.espresso:espresso-core:3.4.0")

    val coroutines = "1.6.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    val dagger = "2.27"
    implementation("com.google.dagger:dagger:$dagger")
    kapt("com.google.dagger:dagger-compiler:$dagger")

    implementation("com.mindorks.android:prdownloader:0.6.0")

    dependencies.add("uiImplementation", project(":openvpn"))
    dependencies.add("skeletonImplementation", project(":openvpn"))

    val coreVersion = "1.2.0"
    implementation("androidx.core:core-ktx:$coreVersion")

    api("io.grpc:grpc-android:1.43.1")
    api("io.grpc:grpc-okhttp:1.43.1")
    api("io.grpc:grpc-core:1.43.1")
    api("io.grpc:grpc-protobuf-lite:1.43.1") {
        exclude(module = "protobuf-lite")
    }
    api("io.grpc:grpc-stub:1.43.1")

    implementation("org.apache.commons:commons-lang3:3.4")

    val work_version = "2.8.0-alpha02"
    implementation("androidx.work:work-runtime-ktx:$work_version")

    api("com.google.protobuf:protobuf-javalite:3.19.1")

    implementation("androidx.annotation:annotation:1.4.0")
    api("javax.annotation:javax.annotation-api:1.3.2")

}

apply(from = "${rootProject.rootDir}/app/proto.gradle")