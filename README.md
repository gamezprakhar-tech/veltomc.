# VELTOMC

A clean, dark, PvP-style client utility mod for Fabric (Minecraft 1.21.11), with a
ClickGUI opened via **Right Shift** (rebindable in the Settings tab).

No cheats: no KillAura, reach, aim assist, auto-clicker, or anti-cheat bypasses. Every
module here is either a HUD/visual readout or a cosmetic/QoL toggle.

## Categories & modules

| Category | Modules |
|---|---|
| Combat | Attack Timer (visual cooldown bar only) |
| Movement | Toggle Sprint, Static FOV |
| Render | Fullbright (safe/vanilla-gamma mode), Zoom, Custom Crosshair |
| Player | Armor HUD, Potion HUD, Auto Respawn |
| HUD | FPS Counter, CPS Counter, Ping, Coordinates, Keystrokes |
| Misc | Chat Timestamps |
| Settings | ClickGUI keybind, accent color, background blur toggle, crosshair style, **config save/load** |

Right-click any module row to rebind its individual hotkey.

## Building it yourself

I can't reach Fabric's Maven servers from this sandbox to actually compile a `.jar`
for you, so here's the full source, ready to drop into a normal dev setup:

1. **Get a scaffold with a Gradle wrapper.** Either:
   - Download the official template from https://fabricmc.net/develop/template/ (pick
     Minecraft 1.21.11), or
   - Clone https://github.com/FabricMC/fabric-example-mod and check out the 1.21.11 branch/tag.
2. **Delete** that template's `src/` folder and its `gradle.properties`, `build.gradle`,
   `settings.gradle`, and `src/main/resources` — replace them with the files in this
   project.
3. **Double-check `gradle.properties`.** I filled in my best current guess for
   `yarn_mappings`, `loader_version`, and `fabric_version` since 1.21.11 is very recent,
   but toolchain versions move fast. Go to https://fabricmc.net/develop/, select
   1.21.11, and paste in whatever it lists if different from mine (takes 30 seconds).
4. Open the folder in IntelliJ IDEA (recommended) and let Gradle sync, or run:
   ```
   ./gradlew build
   ```
5. Your jar will be at `build/libs/veltomc-1.0.0.jar`. Drop it into `.minecraft/mods/`
   alongside a matching **Fabric API** jar for 1.21.11.

## About the one mixin

`MouseMixin` hooks `net.minecraft.client.Mouse#onMouseButton` purely to count clicks for
the CPS/Keystrokes readouts — it doesn't change, cancel, or inject any input. This
method name has been stable across Yarn-mapped versions for years, but if 1.21.11
shifted it, check the current signature at https://linkie.shedaniel.dev (search
"onMouseButton" under Yarn) and adjust the `@Inject(method = "...")` string to match.

## About Fullbright

True zero-shadow fullbright normally needs a mixin into the lighting texture manager.
I went with a safe, mixin-free version instead (it just maxes out the vanilla
brightness/gamma option and restores it on disable), since I couldn't verify the exact
lighting-manager mixin target for 1.21.11 without a live compile. It's a real,
functional brightness boost — just not as extreme as a full lighting-engine override.
Happy to add the stronger version if you confirm the mixin target from Linkie.
