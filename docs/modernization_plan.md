# Modernization Implementation Plan

## User Review Required
> [!IMPORTANT]
> The migration to **Version Catalogs** and **Kotlin DSL** involves significant changes to `build.gradle` files. While these changes are structural, they should not affect the app's runtime behavior.
>
> **Material 3 Migration** changes the UI look and feel and will require visual verification.

## Proposed Changes

### Phase 0: Core Foundation
Establish a stable, modern foundation by updating core platform requirements and compilers.

#### 1. Core Platform Updates
- **Kotlin**: Updated to 2.3.10.
- **AGP**: Updated to 9.0.0.
- **SDK**: Increased `minSdk` to 23, `compileSdk/targetSdk` to 35.

#### 2. Compiler & Plugins
- **Compose Compiler**: Switched to the new Compose compiler plugin.
- **KSP Migration**: Migrated Room and Hilt from Kapt to KSP (required for AGP 9.0).
- **Cleanup**: Fixed deprecated compiler flags (`-opt-in`) and removed redundant manifest package attribute.

#### 3. Library Updates
- **Hilt**: Updated to 2.59.1.
- **Room**: Updated to 2.8.4.
- **KSP**: Updated to 2.3.5.
- **Gradle DSL**: Standardized `app/build.gradle` to use assignment-based DSL.

### Phase 1: Build Infrastructure
Standardize build logic and dependency management.

#### 1. Version Catalog Migration
- Create `gradle/libs.versions.toml`.
- Move all dependencies and plugin versions from `build.gradle` `ext` block to the catalog.
- **Goal**: Centralized dependency management, better IDE autocomplete, faster build configuration.

#### 2. Kotlin DSL Migration
- Convert `build.gradle` -> `build.gradle.kts`.
- Convert `app/build.gradle` -> `app/build.gradle.kts`.
- Convert `settings.gradle` -> `settings.gradle.kts`.
- **Goal**: Type safety, better IDE support, current Android standard.

#### 3. Compose BoM Migration
- Migrate to `androidx.compose:compose-bom:2026.02.00`.
- Remove manual versioning for individual Compose libraries in `libs.versions.toml`.
- **Goal**: Simplified dependency management and guaranteed version compatibility.

### Phase 2: UI & Architecture
Modernize the user interface and navigation stability.

#### 1. Material 3 Migration
- **Current**: Application uses `androidx.compose.material` (Material 2).
- **Proposed**: Migrate to `androidx.compose.material3`.
- **Changes**: Update theme, colors, typography, and components (e.g., `Scaffold`, `TopAppBar`, `Button`).
- **Goal**: Modern Android look, latest component updates (Material 2 is now legacy).

#### 2. Type-Safe Navigation
- **Current**: String-based routes (`"home"`, `"weather/{city}"`).
- **Proposed**: Use Type-Safe Navigation (available in Navigation 2.8.0+).
- **Goal**: Compile-time safety for arguments, no more string hardcoding.

#### 3. Optimization
- Review `collectAsState` vs `collectAsStateWithLifecycle` for Lifecycle-aware collection.

## Verification Plan

### Automated Tests
- Run `./gradlew testDebugUnitTest` to ensure unit tests pass.
- Run `./gradlew assembleDebug` to ensure build succeeds.

### Manual Verification
- **Build Sync**: Ensure Gradle sync completes without errors after each migration step.
- **App Launch**: Launch app on emulator to verify no runtime crashes.
