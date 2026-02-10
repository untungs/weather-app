# Dependency Analysis: Weather App

This document analyzes the current dependencies in the Weather App project and provides guidance for updating them, particularly when migrating from Android Gradle Plugin (AGP) 7.x to AGP 9.x.

## Current Dependency Versions

### Build Configuration

| Dependency | Current Version | Category |
|-----------|-----------------|----------|
| Kotlin | 1.6.10 | Language |
| AGP | 7.1.0 | Build Tool |
| Gradle | 7.2 | Build Tool |
| Compose | 1.1.1 | UI Framework |
| Compose Compiler | 1.1.1 | UI Compiler |
| Retrofit | 2.9.0 | Networking |
| Hilt | 2.41 | DI |
| Room | 2.4.2 | Database |
| Accompanist | 0.24.10-beta | Utilities |

### Android SDK

| Component | Current | Required for AGP 9 |
|-----------|---------|-------------------|
| compileSdk | 32 | 34+ |
| targetSdk | 32 | 34 (recommended) |
| minSdk | 21 | 24 (recommended) |

---

## AGP 7.x → 9.x Migration Impact

### Why Update to AGP 9?

**Benefits:**
- Better build performance with Kotlin DSL support
- Enhanced dependency resolution
- Improved lint checks and security features
- Support for newer Java and Kotlin versions
- Compatibility with latest Android platform features

### Required Gradle Version

| AGP Version | Minimum Gradle Version |
|-------------|----------------------|
| AGP 7.0.x | Gradle 7.0 |
| AGP 7.2.x | Gradle 7.3+ |
| AGP 7.4.x | Gradle 7.5+ |
| AGP 8.0.x | Gradle 8.0+ |
| AGP 8.1.x | Gradle 8.0+ |
| AGP 8.2.x | Gradle 8.2+ |
| AGP 8.3.x | Gradle 8.4+ |
| **AGP 8.4+ / 9.x** | **Gradle 8.6+** |

**Recommendation:** Update to **Gradle 8.7** for AGP 8.5.x compatibility.

---

## Dependency-by-Dependency Analysis

### 1. Kotlin

**Current:** 1.6.10
**Recommended:** 1.9.x or 2.0.x

| Version | AGP Compatibility | Breaking Changes |
|---------|-------------------|------------------|
| 1.6.x | AGP 7.x | None |
| 1.7.x | AGP 7.3+ | Some deprecated APIs |
| 1.8.x | AGP 7.4+ | Kotlinx serialization changes |
| **1.9.x** | AGP 8.0+ | Stable, recommended |
| **2.0.x** | AGP 8.2+ | New K2 compiler, some API changes |

**Required Changes:**
```groovy
// build.gradle
ext {
    kotlin_version = '1.9.24'  // or '2.0.0'
}
```

**Deprecated/Removed Functions:**
- `kotlin.reflect.full.createAnnotation` - Use `KAnnotatedElement.createAnnotation()` instead
- `TypeAliases` - More stable in newer versions
- `@LazySerialization` - No longer needed

---

### 2. Android Gradle Plugin (AGP)

**Current:** 7.1.0
**Recommended:** 8.5.0 or 8.4.0

| AGP Version | Java Version | Key Changes |
|-------------|--------------|-------------|
| 7.1.0 | Java 11 | Baseline |
| 7.2.x | Java 11 | Minor improvements |
| 7.3.x | Java 11 | Variant API changes |
| 7.4.x | Java 11 | **Deprecated** `compile` configuration |
| 8.0.x | Java 17 | **Requires Java 17** |
| 8.1.x | Java 17 | Build config changes |
| 8.2.x | Java 17 | Partial update support |
| 8.3.x | Java 17 | SDK 34 support |
| **8.4.x** | Java 17 | **Recommended stable** |
| **8.5.x** | Java 17 | Latest stable |

**Required Changes:**

```groovy
// build.gradle (project level)
plugins {
    id 'com.android.application' version '8.5.0' apply false
    id 'com.android.library' version '8.5.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.24' apply false
}

// app/build.gradle
android {
    compileSdk 34  // Required for AGP 8.x
    namespace 'com.untungs.weatherapp'  // Required (moved from manifest)
    
    // Deprecated configurations to remove
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17  // Changed from 1_8
        targetCompatibility JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = '17'  // Changed from 1_8
    }
    
    // Remove deprecated packaging options
    packaging {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}
```

**Deprecated/Removed Functions:**

| Deprecated | Replacement | AGP Version |
|------------|-------------|--------------|
| `compile` | `implementation` | 7.4+ |
| `api` | `api` (still exists) | - |
| `applicationVariants` | `applicationVariants` (renamed) | 8.0+ |
| `libraryVariants` | `libraryVariants` (renamed) | 8.0+ |
| `transform` API | `Asm` / `Instrumentation` API | 8.0+ |
| `resValue` in buildType | Still supported | - |
| `ndkPath` | Use `externalNativeBuild` | 7.0+ |

**Critical Changes:**
1. **Java 17 Required** - Must set `JAVA_HOME` to JDK 17
2. **Namespace Required** - Must declare in `build.gradle`, remove from `AndroidManifest.xml`
3. **`compileSdk 34`** - Minimum for AGP 8.x
4. **`buildConfigField` Still Available** - Not deprecated in AGP 8.x

---

### 3. Jetpack Compose

**Current:** 1.1.1
**Recommended:** 1.6.x

| Compose Version | Kotlin Version | AGP Compatibility |
|-----------------|----------------|-------------------|
| 1.1.1 | 1.6.x | 7.x |
| 1.2.0 | 1.7.x | 7.3+ |
| 1.3.x | 1.7.x / 1.8.x | 7.4+ |
| 1.4.x | 1.8.x | 7.4+ |
| **1.5.x** | 1.9.x | 8.0+ |
| **1.6.x** | 1.9.x / 2.0.x | 8.2+ |
| **1.7.x** | 2.0.x | 8.4+ |

**Compose Compiler Extension Version Mapping:**

| Compose Version | Kotlin | Compose Compiler |
|-----------------|--------|------------------|
| 1.1.1 | 1.6.x | 1.1.1 |
| 1.2.0 | 1.7.x | 1.2.0 |
| 1.3.x | 1.7.x / 1.8.x | 1.3.2 |
| 1.4.x | 1.8.x | 1.4.2 |
| **1.5.x** | 1.9.x | 1.5.3 |
| **1.6.x** | 1.9.x / 2.0.x | 1.6.0 |

**Required Changes:**

```groovy
ext {
    compose_version = '1.6.10'  // Latest stable
}
android {
    composeOptions {
        kotlinCompilerExtensionVersion '1.6.0'  // Match Compose version
    }
}
```

**Breaking Changes from 1.1.x to 1.6.x:**

| Change | Impact | Migration |
|--------|--------|-----------|
| `Modifier.drawBehind` | Renamed | Use `Modifier.drawWithContent` |
| `Crossfade` API | Changed | `Crossfade(targetState)` now required |
| `List/Grid` lazy loading | Improved | Some API parameters changed |
| `rememberSaveable` | More stable | No changes needed |
| `Scaffold` padding | Fixed | Check padding calculations |
| `BottomNavBar` | Material 3 | Update imports |

**Deprecated Functions Removed:**
- `androidx.compose.foundation.layout.Stack` - Use `Box` instead
- `androidx.compose.material.icons.filled.**` - Moved to Material3 icons

---

### 4. Compose Navigation

**Current:** 2.5.0-rc01
**Recommended:** 2.7.x

| Navigation Version | Compose Version | AGP Compatibility |
|-------------------|-----------------|-------------------|
| 2.4.x | 1.1.x - 1.2.x | 7.x |
| 2.5.x | 1.2.x - 1.4.x | 7.3+ |
| 2.6.x | 1.4.x+ | 7.4+ |
| **2.7.x** | 1.5.x+ | 8.0+ |

**Required Changes:**

```groovy
implementation "androidx.navigation:navigation-compose:2.7.7"
```

**Breaking Changes:**

| Old API | New API | Version |
|---------|---------|---------|
| `navController.navigate()` with bundle | Use `navController.currentBackStackEntry` + SavedStateHandle | 2.5+ |
| `composable("{arg}")` | `composable("{arg}")` with typed args | 2.6+ |
| `launchSingleTop` | Use `navigationOptions` | 2.6+ |
| `popupTo` | Use `inclusive` parameter | 2.6+ |

---

### 5. Hilt (Dependency Injection)

**Current:** 2.41
**Recommended:** 2.50 or 2.51

| Hilt Version | AGP Compatibility | Kotlin Version |
|-------------|-------------------|----------------|
| 2.41 | 7.x | 1.6.x - 1.8.x |
| 2.44 | 7.4+ | 1.7.x+ |
| 2.48 | 8.0+ | 1.9.x |
| **2.50** | 8.1+ | 1.9.x+ |
| **2.51** | 8.3+ | 1.9.x+ |

**Required Changes:**

```groovy
// build.gradle (project level)
buildscript {
    ext {
        hilt_version = '2.51.1'
    }
    dependencies {
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}

// app/build.gradle
plugins {
    id 'dagger.hilt.android.plugin'
}

dependencies {
    implementation "com.google.dagger:hilt-android:2.51.1"
    kapt "com.google.dagger:hilt-android-compiler:2.51.1"
}
```

**Breaking Changes:**

| Change | Impact | Version |
|--------|--------|---------|
| `@HiltAndroidApp` | Still works | All |
| `@AndroidEntryPoint` | Still works | All |
| `@HiltViewModel` | Still works | All |
| Constructor injection | No changes | - |
| **Kotlin Symbol Processing (KSP)** | Alternative to kapt | 2.48+ |

**Recommendation:** Continue using `kapt` for now, as KSP requires additional configuration.

---

### 6. Room (Database)

**Current:** 2.4.2
**Recommended:** 2.6.x

| Room Version | AGP Compatibility | Key Features |
|-------------|-------------------|--------------|
| 2.4.2 | 7.x | Basic Room |
| 2.4.3 | 7.4+ | Performance fixes |
| 2.5.0 | 7.4+ | Kotlin 1.8+ support |
| 2.5.2 | 8.0+ | Stable |
| **2.6.x** | 8.0+ | KSP support, compile-time checking |

**Required Changes:**

```groovy
ext {
    room_version = '2.6.1'
}

dependencies {
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}
```

**Breaking Changes:**

| Change | Old Behavior | New Behavior | Version |
|--------|--------------|--------------|---------|
| `@Embedded` nullable | Warning | Error in some cases | 2.5+ |
| `@Relation` | Basic support | Enhanced type safety | 2.6+ |
| `Room.inMemoryDatabaseBuilder` | Still works | Still works | - |
| **KSP Support** | Not available | Available | 2.5+ |

---

### 7. Retrofit with Kotlinx Serialization

**Current:** 2.9.0
**Recommended:** 2.11.0 (latest)

| Retrofit Version | OkHttp Version | Notes |
|-----------------|-----------------|-------|
| 2.9.0 | 4.9.x | Current |
| 2.10.x | 4.10.x+ | Updated interceptors |
| **2.11.x** | 4.12.x+ | Latest, better Kotlin support |

**Required Changes:**

```groovy
dependencies {
    implementation "com.squareup.retrofit2:retrofit:2.11.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
    implementation "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0"
}
```

**Breaking Changes:**

| Change | Impact | Version |
|--------|--------|---------|
| `@Url` handling | Fixed behavior | 2.10+ |
| Converter factories | Order matters more | 2.10+ |
| **Suspend functions** | Still fully supported | All |

**Kotlinx Serialization Version Update:**

```groovy
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"  // Was 1.3.3
```

---

### 8. Coil (Image Loading)

**Current:** 2.1.0
**Recommended:** 2.6.x

| Coil Version | Compose Version | Notes |
|--------------|-----------------|-------|
| 2.1.0 | 1.2.x - 1.3.x | Current |
| 2.2.x | 1.3.x - 1.4.x | Updated image pipelines |
| 2.3.x | 1.4.x - 1.5.x | Better caching |
| 2.4.x | 1.5.x+ | Compose-specific improvements |
| **2.5.x** | 1.5.x+ | **Stable** |
| **2.6.x** | 1.6.x+ | Latest |

**Required Changes:**

```groovy
implementation "io.coil-kt:coil-compose:2.6.0"
```

**Breaking Changes:**

| Change | Old API | New API | Version |
|--------|---------|---------|---------|
| `imageRequest` | ` Coil.imageRequest()` | `rememberImagePainter()` | 2.0+ |
| `Crossfade` | Default enabled | Default disabled | 2.4+ |

---

### 9. Accompanist

**Current:** 0.24.10-beta
**Recommended:** 0.34.x

| Accompanist Version | Compose Version | Notes |
|--------------------|-----------------|-------|
| 0.24.x | 1.1.x - 1.2.x | Beta, outdated |
| 0.25.x | 1.2.x - 1.3.x | Stable |
| 0.28.x | 1.3.x+ | Updated APIs |
| 0.30.x | 1.4.x+ | Material 3 support |
| 0.32.x | 1.5.x+ | Current |
| **0.34.x** | 1.6.x+ | Latest stable |

**Required Changes:**

```groovy
ext {
    accompanist_version = '0.34.3'
}

dependencies {
    implementation "com.google.accompanist:accompanist-swiperefresh:$accompanist_version"
    // Remove other accompanist libraries if not used
}
```

**Breaking Changes:**

| Change | Impact | Migration |
|--------|--------|-----------|
| `SwipeRefresh` API | Updated | Check parameter names |
| `rememberSwipeRefreshState` | Still works | No changes needed |
| Permissions | Moved to separate library | `accompanist-permissions` |

---

### 10. Lifecycle & Activity Compose

**Current:** 2.4.1 / 1.4.0
**Recommended:** 2.7.x / 1.9.x

| Lifecycle Version | Compose Version | Notes |
|-------------------|-----------------|-------|
| 2.4.1 | 1.2.x - 1.3.x | Current |
| 2.5.x | 1.3.x - 1.4.x | Updated flows |
| 2.6.x | 1.4.x+ | Better state handling |
| **2.7.x** | 1.5.x+ | Latest stable |

| Activity Version | Compose Version | Notes |
|-------------------|-----------------|-------|
| 1.4.0 | 1.1.x - 1.2.x | Current |
| 1.5.x | 1.2.x - 1.3.x | Updated theming |
| 1.6.x | 1.3.x+ | ComponentActivity changes |
| **1.8.x** | 1.5.x+ | Latest stable |
| **1.9.x** | 1.6.x+ | Latest |

**Required Changes:**

```groovy
dependencies {
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.8.0'
    implementation 'androidx.activity:activity-compose:1.9.0'
}
```

**Breaking Changes:**

| Change | Old Behavior | New Behavior | Version |
|--------|--------------|--------------|---------|
| `collectAsState` | Lazy collection | More efficient | 2.6+ |
| `DisposableEffect` | Basic cleanup | Enhanced | 2.6+ |
| `rememberSaveable` | Map-based | Better state handling | 2.6+ |

---

## Complete Migration Checklist

### Phase 1: Preparation

1. **Backup project** (git commit)
2. **Install JDK 17** - Required for AGP 8.x
3. **Update Gradle Wrapper:**
   ```bash
   ./gradlew wrapper --gradle-version 8.7
   ```

### Phase 2: Build Configuration Updates

1. **Update `build.gradle` (project level):**
   ```groovy
   buildscript {
       ext {
           kotlin_version = '1.9.24'
           compose_version = '1.6.10'
           hilt_version = '2.51.1'
           room_version = '2.6.1'
           accompanist_version = '0.34.3'
       }
   }
   
   plugins {
       id 'com.android.application' version '8.5.0' apply false
       id 'com.android.library' version '8.5.0' apply false
       id 'org.jetbrains.kotlin.android' version '1.9.24' apply false
   }
   ```

2. **Update `app/build.gradle`:**
   ```groovy
   android {
       compileSdk 34
       namespace 'com.untungs.weatherapp'
       
       compileOptions {
           sourceCompatibility JavaVersion.VERSION_17
           targetCompatibility JavaVersion.VERSION_17
       }
       
       kotlinOptions {
           jvmTarget = '17'
       }
       
       composeOptions {
           kotlinCompilerExtensionVersion '1.6.0'
       }
   }
   ```

### Phase 3: Dependency Updates

Update all dependencies to recommended versions:

```groovy
dependencies {
    // AndroidX Core
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.activity:activity-compose:1.9.0'
    
    // Compose
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
    implementation "androidx.compose.material:material:$compose_version"
    
    // Navigation
    implementation "androidx.navigation:navigation-compose:2.7.7"
    implementation "androidx.hilt:hilt-navigation-compose:1.2.0"
    
    // Lifecycle
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0'
    
    // Accompanist
    implementation "com.google.accompanist:accompanist-swiperefresh:$accompanist_version"
    
    // Coil
    implementation "io.coil-kt:coil-compose:2.6.0"
    
    // Room
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    
    // Networking
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
    implementation "com.squareup.retrofit2:retrofit:2.11.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
    implementation "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0"
    
    // Hilt
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0"
    testImplementation "app.cash.turbine:turbine:1.1.0"
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
    debugImplementation "androidx.compose.ui:ui-tooling:$compose_version"
}
```

### Phase 4: Code Updates

#### 4.1 Remove `android:label` from Manifest

If using `namespace` in build.gradle, remove from `AndroidManifest.xml`:
```xml
<!-- Remove or update -->
<application
    android:name=".WeatherApplication"
    android:label="@string/app_name"  <!-- Keep this -->
    ...
>
```

#### 4.2 Update Java/Kotlin Target

Ensure all Kotlin code compiles with JVM 17:
```bash
./gradlew clean build
```

#### 4.3 Fix Deprecated APIs

**Common fixes needed:**

1. **Coil Image Loading:**
   ```kotlin
   // Old
   Image(
       painter = rememberImagePainter(data = url),
       contentDescription = null
   )
   
   // New (Coil 2.x)
   AsyncImage(
       model = url,
       contentDescription = null
   )
   ```

2. **Navigation Args:**
   ```kotlin
   // Ensure proper type handling
   composable(
       route = "weather/{city}/{lat}/{lon}",
       arguments = listOf(
           navArgument("city") { type = NavType.StringType },
           navArgument("lat") { type = NavType.FloatType },
           navArgument("lon") { type = NavType.FloatType }
       )
   ) { backStackEntry ->
       val city = backStackEntry.arguments?.getString("city") ?: ""
       val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
       val lon = backStackEntry.arguments?.getFloat("lon") ?: 0f
   }
   ```

3. **Hilt ViewModel:**
   ```kotlin
   // Ensure proper injection
   @HiltViewModel
   class HomeViewModel @Inject constructor(
       private val locationRepository: LocationRepository,
       private val weatherRepository: WeatherRepository
   ) : ViewModel()
   ```

### Phase 5: Testing

1. **Run all unit tests:**
   ```bash
   ./gradlew test
   ```

2. **Run lint:**
   ```bash
   ./gradlew lint
   ```

3. **Verify build:**
   ```bash
   ./gradlew assembleDebug
   ./gradlew assembleRelease
   ```

---

## Known Issues & Troubleshooting

### Issue: "Package not found" after namespace migration

**Solution:** Ensure `namespace` is correctly set in `build.gradle` and remove `android:label` from `AndroidManifest.xml`.

### Issue: Kotlinx Serialization not working

**Solution:** Ensure plugin is applied:
```groovy
plugins {
    id 'kotlinx-serialization'
}
```

And in `build.gradle` (app):
```groovy
apply plugin: 'kotlinx-serialization'
```

### Issue: Hilt kapt conflicts

**Solution:** If using Room with kapt and Hilt with kapt, ensure correct order:
```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}
```

### Issue: Compose Preview not working

**Solution:** Update `ui-tooling` and ensure JDK 17 is used for Android Studio.

### Issue: Gradle 8.x incompatibility with Java 11

**Solution:** Must use Java 17 for AGP 8.x. Update `JAVA_HOME`:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

---

## Security Considerations

### Current Issue: Hardcoded API Key

**Location:** `NetworkInterceptor.kt:75`

```kotlin
.addQueryParameter("appid", "[apikey]")
```

**Recommended Fix:**
```kotlin
// Use BuildConfig
.addQueryParameter("appid", BuildConfig.API_KEY)
```

**Add to `build.gradle`:**
```groovy
buildConfigField "String", "API_KEY", "\"${project.hasProperty('API_KEY') ? project.property('API_KEY') : 'default_key'}\""
```

**Use `local.properties`:**
```properties
# local.properties (NOT committed to git)
API_KEY=your_api_key_here
```

**Add to `.gitignore`:**
```
# local.properties
local.properties
```

---

## Testing Recommendations

After updating dependencies:

1. **Unit Tests:**
   - ViewModel tests (Turbine + Coroutines Test)
   - Repository tests with mocked data
   - Extension function tests

2. **Instrumented Tests:**
   - Compose UI tests
   - Navigation tests
   - Database operations tests

3. **Manual Testing:**
   - Weather data loading
   - Search functionality
   - Favorite location management
   - Pull-to-refresh
   - Dark/Light theme

---

## Summary of Required Updates

| Dependency | Current | Recommended | Reason |
|------------|---------|-------------|--------|
| Gradle | 7.2 | 8.7 | AGP 8.5 requires 8.6+ |
| AGP | 7.1.0 | 8.5.0 | Modern build features |
| Kotlin | 1.6.10 | 1.9.24 | Compose 1.6.x compatibility |
| Compose | 1.1.1 | 1.6.10 | Modern APIs + performance |
| Compose Compiler | 1.1.1 | 1.6.0 | Match Compose version |
| Navigation | 2.5.0-rc01 | 2.7.7 | Stable + Compose 1.6 support |
| Hilt | 2.41 | 2.51.1 | Kotlin 1.9 support |
| Room | 2.4.2 | 2.6.1 | Better compile-time checks |
| Retrofit | 2.9.0 | 2.11.0 | Latest features |
| Coil | 2.1.0 | 2.6.0 | Compose optimizations |
| Accompanist | 0.24.10-beta | 0.34.3 | Stable release |
| Lifecycle | 2.4.1 | 2.8.0 | Compose 1.6 compatibility |
| Activity Compose | 1.4.0 | 1.9.0 | Latest stable |
| Core KTX | 1.8.0 | 1.13.1 | Latest AndroidX |
| Kotlinx Serialization | 1.3.3 | 1.6.3 | Security + bug fixes |
| OkHttp Logging | 4.9.3 | 4.12.0 | Security patches |

**Estimated Migration Time:** 2-4 hours for dependency updates, 1-2 hours for testing and fixes.

---

## References

- [AGP Release Notes](https://developer.android.com/studio/build)
- [Jetpack Compose Versions](https://developer.android.com/jetpack-compose/releases)
- [Hilt Version Compatibility](https://dagger.dev/hilt/gradle-setup)
- [Room Release Notes](https://developer.android.com/jetpack/androidx/releases/room)
- [Retrofit Changelog](https://github.com/square/retrofit/blob/master/CHANGELOG.md)
- [Kotlin Changelog](https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md)
