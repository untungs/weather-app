# Dependency Migration Plan

This document provides a step-by-step checklist for migrating the Weather App dependencies. Each step is designed to be **buildable as a standalone commit** - build the project after each step to verify success before proceeding.

## Prerequisites

Before starting, ensure:
- JDK 17 is installed and `JAVA_HOME` is set correctly
- You have a clean git working directory
- You understand that each step ends with a buildable project

```bash
# Verify Java version
java -version

# Expected output should show version 17.x
```

---

## Step 0: Gradle + AGP Updates

**Priority:** Critical - Required for all other updates

**Risk:** High - Foundation changes with breaking updates

**Changes Applied:**
- Gradle: 7.2 → 8.7
- AGP: 7.1.0 → 8.5.0
- Added namespace declaration
- Added buildConfig feature flag

### Changes

**`gradle/wrapper/gradle-wrapper.properties`:**

Update Gradle from 7.2 to 8.7:

```bash
./gradlew wrapper --gradle-version 8.7
```

**`build.gradle` (project level):**

```groovy
plugins {
    id 'com.android.application' version '8.5.0' apply false
    id 'com.android.library' version '8.5.0' apply false
    // Kotlin remains at 1.6.10 for now (updated in Step 1)
    id 'org.jetbrains.kotlin.android' version "1.6.10" apply false
}
```

**`app/build.gradle`:**

```groovy
android {
    namespace 'com.untungs.weatherapp'

    buildFeatures {
        buildConfig true
    }

    defaultConfig {
        applicationId "com.untungs.weatherapp"
        minSdk 21
        targetSdk 32  // Will update to 34 in Step 1
        versionCode 1
        versionName "1.0"
        // ... rest unchanged
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }

    // Update deprecated packaging
    packaging {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}
```

### Verify

```bash
./gradlew --version
# Should show: Gradle 8.7

./gradlew clean assembleDebug
# Project should compile successfully
```

### Expected Outcome
- Gradle 8.7 installed
- AGP 8.5.0 configured
- Namespace declared (resolves AGP 8.0+ requirement)
- BuildConfig generation enabled
- Project compiles with Java 17

### Common Issues

| Issue | Solution |
|-------|----------|
| `IncrementalTaskInputs` error | Ensure AGP 8.5.0 is used with Gradle 8.7 |
| `Namespace not specified` | Add `namespace` to app/build.gradle |
| `BuildConfig fields disabled` | Add `android.buildFeatures.buildConfig true` |
| `Java version mismatch` | Verify `JAVA_HOME` points to JDK 17 |

### Commit

```
refactor: update Gradle to 8.7 and AGP to 8.5.0

- Gradle 7.2 → 8.7
- AGP 7.1.0 → 8.5.0
- Added namespace declaration
- Enabled BuildConfig feature
- Configured Java 17 target
```

---

## Step 1: Kotlin + SDK Updates

**Priority:** Critical - Kotlin and SDK foundation

**Risk:** High - Kotlin version bump

**Dependencies Updated:**
- Kotlin: 1.6.10 → 1.9.24
- compileSdk: 32 → 34
- targetSdk: 32 → 34

### Changes

**`build.gradle` (project level):**

```groovy
buildscript {
    ext {
        kotlin_version = '1.9.24'
        compose_version = '1.1.1'  // Keep unchanged for now
        retrofit_version = '2.9.0'  // Keep unchanged for now
        hilt_version = '2.41'       // Keep unchanged for now
        accompanist_version = '0.24.10-beta'  // Keep unchanged for now
        room_version = '2.4.2'      // Keep unchanged for now
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-serialization:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}

plugins {
    id 'com.android.application' version '8.5.0' apply false  // Already updated in Step 0
    id 'com.android.library' version '8.5.0' apply false  // Already updated in Step 0
    id 'org.jetbrains.kotlin.android' version "$kotlin_version" apply false
}
```

**`app/build.gradle`:**

```groovy
android {
    compileSdk 34
    namespace 'com.untungs.weatherapp'  // Already added in Step 0

    defaultConfig {
        applicationId "com.untungs.weatherapp"
        minSdk 21
        targetSdk 34
        versionCode 1
        versionName "1.0"
        // ... rest unchanged
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }

    composeOptions {
        kotlinCompilerExtensionVersion '1.1.1'  // Keep unchanged for now
    }

    buildFeatures {
        buildConfig true  // Already added in Step 0
    }

    // Update deprecated packaging
    packaging {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Project compiles successfully with Kotlin 1.9.24
- SDK 34 targets work correctly
- Java 17 compatibility confirmed

### Common Issues

| Issue | Solution |
|-------|----------|
| `Java version mismatch` | Verify `JAVA_HOME` points to JDK 17 |
| `Kotlin plugin error` | Ensure `kotlin_version` is correctly set |

### Commit

```
refactor: update Kotlin to 1.9.24 and SDK to 34

- Kotlin 1.6.10 → 1.9.24
- compileSdk: 32 → 34
- targetSdk: 32 → 34
```

---

## Step 2: AndroidX Core Updates

**Priority:** High - Required for Compose compatibility

**Risk:** Medium

**Dependencies Updated:**
- Core KTX: 1.8.0 → 1.13.1
- Activity Compose: 1.4.0 → 1.9.0

### Changes

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.activity:activity-compose:1.9.0'
    // ... keep other existing dependencies unchanged
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Project compiles
- Activity Compose 1.9.0 compatible with AGP 8.5.0

### Commit

```
chore: update AndroidX core dependencies

- core-ktx: 1.8.0 → 1.13.1
- activity-compose: 1.4.0 → 1.9.0
```

---

## Step 3: Lifecycle + ViewModel

**Priority:** High - Compose ViewModel integration

**Risk:** Medium

**Dependencies Updated:**
- Lifecycle Runtime KTX: 2.4.1 → 2.8.0
- Lifecycle ViewModel Compose: 2.4.1 → 2.8.0
- Lifecycle Runtime Compose: (new) → 2.8.0

### Changes

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.8.0'
    // ... keep other dependencies unchanged
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Lifecycle 2.8.0 integrates with Compose
- ViewModel still works with Hilt

### Commit

```
chore: update lifecycle dependencies to 2.8.0

- lifecycle-runtime-ktx: 2.4.1 → 2.8.0
- lifecycle-viewmodel-compose: 2.4.1 → 2.8.0
- Added lifecycle-runtime-compose:2.8.0
```

---

## Step 4: Jetpack Compose (Major Version Bump)

**Priority:** Critical - Largest impact on UI code

**Risk:** High - Breaking changes in Compose APIs

**Dependencies Updated:**
- Compose: 1.1.1 → 1.6.10
- Compose Compiler: 1.1.1 → 1.6.0

### Changes

**`build.gradle` (project level):**

```groovy
ext {
    compose_version = '1.6.10'
    // ... keep other versions unchanged
}
```

**`app/build.gradle`:**

```groovy
android {
    composeOptions {
        kotlinCompilerExtensionVersion '1.6.0'
    }
}
```

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
    implementation "androidx.compose.material:material:$compose_version"
    // ... keep other dependencies unchanged
}
```

### Code Fixes Required

After updating Compose, if compilation fails with deprecation warnings:

1. **Scaffold padding (if any):**
   ```kotlin
   // May need review if using Scaffold padding values
   Scaffold { innerPadding ->
       // innerPadding handling unchanged but verify calculations
   }
   ```

2. **Crossfade API (if used):**
   ```kotlin
   // Check usage of Crossfade composable
   Crossfade(targetState = value) { state ->
       // Already compatible in 1.6.x
   }
   ```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Project compiles with Compose 1.6.10
- UI renders correctly

### Common Issues

| Issue | Solution |
|-------|----------|
| `Modifier.drawBehind` | Use `Modifier.drawWithContent` if referenced |
| `Stack` composable | Replace with `Box` |
| Material icons import | Update to Material3 icons package |

### Commit

```
feat: update compose to 1.6.10

- compose: 1.1.1 → 1.6.10
- compose compiler: 1.1.1 → 1.6.0
- Updated Compose dependencies
```

---

## Step 5: Compose Navigation

**Priority:** High - Navigation integration with Compose

**Risk:** Medium

**Dependencies Updated:**
- Navigation Compose: 2.5.0-rc01 → 2.7.7
- Hilt Navigation Compose: 1.0.0 → 1.2.0

### Changes

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    implementation "androidx.navigation:navigation-compose:2.7.7"
    implementation "androidx.hilt:hilt-navigation-compose:1.2.0"
    // ... keep other dependencies unchanged
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Navigation 2.7.7 works with Compose 1.6.10
- Hilt navigation integration works

### Commit

```
chore: update navigation and hilt-navigation-compose

- navigation-compose: 2.5.0-rc01 → 2.7.7
- hilt-navigation-compose: 1.0.0 → 1.2.0
```

---

## Step 6: Hilt (Dependency Injection)

**Priority:** High - Core DI framework

**Risk:** Medium

**Dependencies Updated:**
- Hilt: 2.41 → 2.51.1

### Changes

**`build.gradle` (project level):**

```groovy
ext {
    hilt_version = '2.51.1'
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Hilt 2.51.1 generates components correctly
- All `@Inject`, `@HiltViewModel`, `@AndroidEntryPoint` work

### Commit

```
chore: update hilt to 2.51.1

- hilt: 2.41 → 2.51.1
- Supports Kotlin 1.9.x and AGP 8.5.0
```

---

## Step 7: Room (Database)

**Priority:** High - Local data persistence

**Risk:** Medium

**Dependencies Updated:**
- Room: 2.4.2 → 2.6.1

### Changes

**`build.gradle` (project level):**

```groovy
ext {
    room_version = '2.6.1'
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Room 2.6.1 generates DAOs correctly
- No breaking changes for current entity definitions

### Commit

```
chore: update room to 2.6.1

- room: 2.4.2 → 2.6.1
- Improved compile-time checks
```

---

## Step 8: Networking Stack

**Priority:** Medium - API communication

**Risk:** Low - Retrofit/OkHttp have good backwards compatibility

**Dependencies Updated:**
- Retrofit: 2.9.0 → 2.11.0
- OkHttp Logging: 4.9.3 → 4.12.0
- Kotlinx Serialization: 1.3.3 → 1.6.3
- Serialization Converter: 0.8.0 → 1.0.0

### Changes

**`build.gradle` (project level):**

```groovy
ext {
    retrofit_version = '2.11.0'
}
```

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
    implementation "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0"
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
    // ... keep other dependencies unchanged
}
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Network calls work correctly
- Serialization handles JSON responses

### Commit

```
chore: update networking dependencies

- retrofit: 2.9.0 → 2.11.0
- okhttp logging: 4.9.3 → 4.12.0
- kotlinx serialization: 1.3.3 → 1.6.3
- serialization converter: 0.8.0 → 1.0.0
```

---

## Step 9: UI Utilities (Coil + Accompanist)

**Priority:** Medium - Image loading and UI helpers

**Risk:** Medium - Coil has some breaking changes

**Dependencies Updated:**
- Coil: 2.1.0 → 2.6.0
- Accompanist: 0.24.10-beta → 0.34.3

### Changes

**`build.gradle` (project level):**

```groovy
ext {
    accompanist_version = '0.34.3'
}
```

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    implementation "com.google.accompanist:accompanist-swiperefresh:$accompanist_version"
    implementation "io.coil-kt:coil-compose:2.6.0"
    // ... keep other dependencies unchanged
}
```

### Code Fixes Required

**Coil Image Loading:**
```kotlin
// OLD (Coil 2.1.0)
Image(
    painter = rememberImagePainter(data = url),
    contentDescription = null
)

// NEW (Coil 2.6.0) - AsyncImage is recommended
AsyncImage(
    model = url,
    contentDescription = null
)
```

### Verify

```bash
./gradlew clean assembleDebug
```

### Expected Outcome
- Images load correctly with AsyncImage
- SwipeRefresh works with Accompanist 0.34.3

### Common Issues

| Issue | Solution |
|-------|----------|
| `rememberImagePainter` | Replace with `AsyncImage` |
| `Crossfade` disabled by default | Set `crossfade(true)` if needed |

### Commit

```
feat: update coil to 2.6.0 and accompanist to 0.34.3

- coil-compose: 2.1.0 → 2.6.0
- accompanist: 0.24.10-beta → 0.34.3
- Updated to use AsyncImage for image loading
```

---

## Step 10: Testing Dependencies

**Priority:** Medium - Ensure test compatibility

**Risk:** Low

**Dependencies Updated:**
- JUnit: 4.13.2 → 4.13.2 (unchanged, verify)
- Coroutines Test: 1.6.2 → 1.8.0
- Turbine: 0.8.0 → 1.1.0
- AndroidX Test Ext JUnit: 1.1.3 → 1.1.5
- Espresso Core: 3.4.0 → 3.5.1
- Compose Test JUnit: 1.1.1 → 1.6.10 (match Compose version)

### Changes

**`app/build.gradle` dependencies:**

```groovy
dependencies {
    testImplementation 'junit:junit:4.13.2'
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0"
    testImplementation "app.cash.turbine:turbine:1.1.0"
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
    // ... keep other dependencies unchanged
}
```

### Verify

```bash
./gradlew testDebugUnitTest
```

### Expected Outcome
- All unit tests pass
- Turbine and coroutines tests work

### Commit

```
chore: update testing dependencies

- kotlinx-coroutines-test: 1.6.2 → 1.8.0
- turbine: 0.8.0 → 1.1.0
- androidx test ext junit: 1.1.3 → 1.1.5
- espresso core: 3.4.0 → 3.5.1
- compose test junit: → 1.6.10 (matching compose version)
```

---

## Step 11: Cleanup and Final Verification

**Priority:** Low - Final polish

**Risk:** Low

### Changes

1. **Remove deprecated `kotlin-serialization` classpath** (if using plugins DSL):
   ```groovy
   // build.gradle - remove this line
   classpath "org.jetbrains.kotlin:kotlin-serialization:$kotlin_version"
   ```

2. **Ensure plugins DSL includes serialization:**
   ```groovy
   // build.gradle (app)
   plugins {
       id 'com.android.application'
       id 'org.jetbrains.kotlin.android'
       id 'kotlin-kapt'
       id 'kotlinx-serialization'
       id 'dagger.hilt.android.plugin'
   }
   ```

3. **Update `proguard-rules.pro`** if needed for newer libraries

### Final Build Verification

```bash
# Clean build
./gradlew clean

# Assemble debug
./gradlew assembleDebug

# Run tests
./gradlew testDebugUnitTest

# Run lint
./gradlew lint
```

### Commit

```
chore: final dependency cleanup and verification

- Removed deprecated serialization classpath
- Verified all dependencies compatible
- All tests passing
```

---

## Dependency Update Summary by Architecture Layer

### Build System
| Dependency | From   | To     | Step |
|------------|--------|--------|------|
| Gradle     | 7.2    | 8.7    | 0    |
| AGP        | 7.1.0  | 8.5.0  | 0    |
| Kotlin     | 1.6.10 | 1.9.24 | 1    |
| compileSdk | 32     | 34     | 1    |
| targetSdk  | 32     | 34     | 1    |

### Data Layer
| Dependency | From | To | Step |
|------------|------|-----|------|
| Room | 2.4.2 | 2.6.1 | 7 |
| Retrofit | 2.9.0 | 2.11.0 | 8 |
| OkHttp Logging | 4.9.3 | 4.12.0 | 8 |
| Kotlinx Serialization | 1.3.3 | 1.6.3 | 8 |
| Hilt | 2.41 | 2.51.1 | 6 |

### UI Layer
| Dependency | From | To | Step |
|------------|------|-----|------|
| Compose | 1.1.1 | 1.6.10 | 4 |
| Compose Compiler | 1.1.1 | 1.6.0 | 4 |
| Navigation | 2.5.0-rc01 | 2.7.7 | 5 |
| Lifecycle | 2.4.1 | 2.8.0 | 3 |
| Activity Compose | 1.4.0 | 1.9.0 | 2 |
| Core KTX | 1.8.0 | 1.13.1 | 2 |
| Coil | 2.1.0 | 2.6.0 | 9 |
| Accompanist | 0.24.10-beta | 0.34.3 | 9 |
| Hilt Nav Compose | 1.0.0 | 1.2.0 | 5 |

### Testing
| Dependency | From | To | Step |
|------------|------|-----|------|
| Coroutines Test | 1.6.2 | 1.8.0 | 10 |
| Turbine | 0.8.0 | 1.1.0 | 10 |
| Test Ext JUnit | 1.1.3 | 1.1.5 | 10 |
| Espresso Core | 3.4.0 | 3.5.1 | 10 |

---

## Rollback Plan

If any step fails to build:

```bash
# Revert to previous commit
git checkout <previous-step-commit>

# Or revert specific file
git checkout HEAD~1 -- build.gradle
git checkout HEAD~1 -- app/build.gradle
```

---

## Estimated Timeline

| Step | Estimated Time | Risk Level |
|------|----------------|------------|
| Step 0: Gradle + AGP | 15 min | High |
| Step 1: Kotlin + SDK | 10 min | High |
| Step 2: AndroidX Core | 5 min | Medium |
| Step 3: Lifecycle | 5 min | Medium |
| Step 4: Compose | 15 min | High |
| Step 5: Navigation | 5 min | Medium |
| Step 6: Hilt | 5 min | Medium |
| Step 7: Room | 5 min | Medium |
| Step 8: Networking | 5 min | Low |
| Step 9: UI Utilities | 10 min | Medium |
| Step 10: Testing | 5 min | Low |
| Step 11: Cleanup | 5 min | Low |

**Total Estimated Time:** ~1.5 - 2 hours

---

## Verification Commands

Use these commands after each step to verify success:

```bash
# Quick build check
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Check for lint issues
./gradlew lintDebug

# Full verification (final step)
./gradlew clean build
```

---

## Post-Migration Tasks

1. **Security Fix (Optional but recommended):**
   - Move API key to `local.properties` or environment variables
   - See `docs/analysis.md` for details

2. **Performance Verification:**
   ```bash
   ./gradlew assembleRelease
   # Verify APK size is reasonable
   ```

3. **Test on Physical Device:**
   - Verify UI renders correctly
   - Test network calls
   - Test database operations
