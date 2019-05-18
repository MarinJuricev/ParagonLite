object Versions {
    const val kotlin = "1.3.31"
    const val navigation = "1.0.0"
    const val coroutines = "1.1.1"
    const val lifecycle = "2.0.0"
    const val koin = "2.0.0-GA6"
    const val room = "2.1.0-beta01"
}

object Dependencies {
    val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    val navigationFragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
    val navigationFragmentKtx = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navigationUi = "android.arch.navigation:navigation-ui:${Versions.navigation}"
    val navigationUiKtx = "android.arch.navigation:navigation-ui-ktx:${Versions.navigation}"

    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    val lifecyleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val lifeCycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    val koinCore = "org.koin:koin-core:${Versions.koin}"
    val koinCoreExt = "org.koin:koin-core-ext:${Versions.koin}"
    val koinAndrodixScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    val koinAndroidxViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val koinTest = "org.koin:koin-test:${Versions.koin}"

    val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
}