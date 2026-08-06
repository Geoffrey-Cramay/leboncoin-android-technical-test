# Technical Decisions

## Build Toolchain

### AGP 8.13.2 / Kotlin 2.4.10
Latest stable pair that builds clean against this project's dependency set (Spark, Coil3, Room, Hilt). AGP 9.x exists but requires a Hilt bump (2.59+) and breaks Spark's transitive deps (see below): a deliberately separate decision, not bundled in here.

### KSP over kapt
kapt relies on the K1 stub-generation pipeline. KSP is the supported annotation processing path for both Hilt and Room going forward, and is already what this project uses.

### Java 17 toolchain
**kotlin { jvmToolchain(17) }**, auto-provisioned via **foojay-resolver-convention**: pins the actual JDK Gradle runs on, independent of the host machine's **JAVA_HOME**. Needed in practice: this machine's local JDK (25) crashes the Gradle 8.13 daemon outright, so relying on **JAVA_HOME** wasn't an option.

### Hilt pinned to 2.58
2.59+ requires AGP 9.0+/Gradle 9.1+, which this project isn't on (see above).

### compileSdk 36 / targetSdk 35
**compileSdk** bumped to satisfy **activity-compose**/**core-ktx**'s own AAR metadata floor. **targetSdk** intentionally left lower: compileSdk unlocks APIs to compile against, targetSdk opts into new runtime behavior; nothing here needs API 36 runtime behavior yet.

### Spark stayed at 1.4.0
Spark 3.4.0 transitively hard-pins **activity-compose:1.13.0** (via **filekit-dialogs-compose**), which needs AGP 9.1, not yet stable. Downgrading **activity-compose** directly doesn't help; Spark's transitive pin overrides it. Revisit once AGP 9.1 is stable or Spark drops the filekit dependency.

---

## Architecture

### MVVM over MVI
The app is a list + detail + favorite-toggle, one upstream **Flow<Result<T>>** per screen, folded into a sealed **UiState**. MVI's ceremony (Intent classes, Reducer, unified state object) buys nothing here since there's no complex multi-source state to reconcile.

### Multi-module split (**domain**, **data**, **presentation**, **design-system**)
Not driven by build-time or team-ownership needs at this app's size, done to demonstrate the pattern the README explicitly grades. Dependency direction is strictly one-way: **:app** -> **:presentation:*** -> **:domain** <- **:data:repository** -> **:data:remote**/**:data:local**. Presentation never touches **:data:*** directly.

### Offline-first via **channelFlow**, no separate **refresh()**
**TrackRepository.getAllTracks()** is the only read entry point. It forwards Room's **Flow** and, whenever the cache is empty, concurrently triggers a background fetch that writes into Room, Room's own re-emission is what pushes fresh data to the UI, no manual invalidation needed. Known limitation: only refreshes on an empty cache, never a stale one (fine for a static test endpoint).

### **Result<T>** over throwing, **TrackError** sealed hierarchy
Every repository function returns **Result<T>** instead of letting exceptions propagate through Flow. The README explicitly rejects an app that crashes systematically, so no failure path (network, disk, missing row) is allowed to throw past the repository boundary.

### No separate **TrackUiModel**
**Track** already has exactly the fields both screens render, a field-identical UI model would add a mapping layer with no behavioral difference. Revisit if the UI ever needs a field with no domain meaning.

### Hilt Gradle plugin applied only in **:app**
Its two jobs (the **@AndroidEntryPoint** bytecode transform, and aggregating every **@Module** into the final component) are anchored to wherever **@HiltAndroidApp** lives, only **:app**. Other modules declare **@Module**/**@Inject** but don't need the plugin itself.

---

## Bugs fixed in the original scaffold

- **GlobalScope.launch** with an empty catch in the list ViewModel: switched to **viewModelScope**, errors now surface to the UI.
- **MutableSharedFlow(replay = 0)** as the list holder: late collectors (e.g. after rotation) missed emissions; replaced with **StateFlow**.
- HTTP logging interceptor condition inverted (**if (!BuildConfig.DEBUG)**): logged bodies in release, nothing in debug; flipped.
- Detail screen was a static placeholder; no id was ever passed via **Intent**: wired up end to end.
- Detail activity had its own **MAIN**/**LAUNCHER** filter and **exported="true"**: any app could jump straight to it, skipping the required id; removed the filter, set **exported="false"**.
- List item reused the same **Modifier** on both the root **Card** and the inner image: split so a caller's modifier only applies to the root.
- **AnalyticsHelper** held a raw **Activity** reference past its lifecycle (LeakCanary bait): switched to **applicationContext**, then structurally fixed by constructor-injecting **@ApplicationContext** via Hilt.
- **composeBom** pinned a year ahead of what Spark 1.4.0 was built against: **NoSuchMethodError** crash on launch, invisible to **compileKotlin**/**assembleDebug**/unit tests, only caught by actually running the app. Downgraded to match Spark's own declared BOM version.

---

## Testing

### Paparazzi over Roborazzi and Compose UI Test
The app's UI states (list item, detail content, error view) are static and composable in isolation: no interaction needed to render them. Paparazzi renders via Android's real Layoutlib on the JVM, no emulator, fast and CI-friendly. Roborazzi was ruled out: it layers on Robolectric, adding compatibility overhead for no benefit here. Compose UI Test was ruled out for snapshotting specifically: it needs a device/emulator and is built for interaction testing, not rendering verification.

Note: **2.0.0-alpha04** is the only Paparazzi release that builds against this project's toolchain (no stable release supports **compileSdk 36**). Two workarounds needed: the **Test** task's JDK pinned to 21 (Paparazzi's own jars require it, project compile target stays at 17), and **reports.html.required.set(false)** (Paparazzi's HTML reporter references a Gradle-internal class missing on Gradle 8.13).

**Running the snapshot tests** (per module: **:designsystem**, **:presentation:tracks**, **:presentation:details**):
```
./gradlew :presentation:tracks:recordPaparazziDebug   # record/update goldens
./gradlew :presentation:tracks:verifyPaparazziDebug    # verify against goldens (CI check)
```
Goldens live under each module's **src/test/snapshots/images/** and are committed alongside the code change.

### Turbine for ViewModel **StateFlow**/**Flow** assertions
Used across ViewModel tests instead of the manual **launch { flow.collect {} }** + **advanceUntilIdle()** pattern, for readable multi-emission assertions (e.g. asserting **Loading** -> **Success** -> **Error** in sequence).

### Configuration changes: verified manually, not with an instrumented test
**StateFlow** in a **@HiltViewModel** survives rotation by construction (Android retains the **ViewModelStore**); the detail screen's track id comes from **SavedStateHandle**, which survives process death too. Verified by hand: rotating on both the list and detail screens (mid-loading and after data loaded), and rotating after toggling a favorite: no crash, no data loss, correct state on screen. An automated **ActivityScenario.recreate()** test would be the natural next step if this project kept growing.

---

## Evolution Ideas

- **Stale-cache refresh / pull-to-refresh**: today only an empty cache triggers a refetch.
- **Automated config-change tests**: **ActivityScenario.recreate()** on both screens.
- **Search, filter-by-favorite, pagination**: not needed for one static JSON response, would be for a growing catalog.
- **Mono-activity + Compose Navigation**: the current two-Activity setup stops scaling past a couple of screens.
- **build-logic convention plugins**: remove the repeated **android {}**/**kotlin {}** blocks across 7 modules.
- **CI**: **./gradlew test assembleDebug** + Paparazzi verify on every PR; would have caught the composeBom crash above, which passed every build/test task and only broke on-device.
