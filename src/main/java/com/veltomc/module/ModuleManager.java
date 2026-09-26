package com.veltomc.module;

import com.veltomc.module.impl.combat.AttackTimerModule;
import com.veltomc.module.impl.hud.CoordinatesModule;
import com.veltomc.module.impl.hud.CpsCounterModule;
import com.veltomc.module.impl.hud.FpsCounterModule;
import com.veltomc.module.impl.hud.KeystrokesModule;
import com.veltomc.module.impl.hud.PingModule;
import com.veltomc.module.impl.misc.ChatTimestampsModule;
import com.veltomc.module.impl.movement.StaticFovModule;
import com.veltomc.module.impl.movement.ToggleSprintModule;
import com.veltomc.module.impl.player.ArmorHudModule;
import com.veltomc.module.impl.player.AutoRespawnModule;
import com.veltomc.module.impl.player.PotionHudModule;
import com.veltomc.module.impl.render.CrosshairModule;
import com.veltomc.module.impl.render.FullbrightModule;
import com.veltomc.module.impl.render.ZoomModule;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central registry for every VELTOMC module. Handles registration, per-category
 * lookup for the ClickGUI, per-module keybind polling, and the tick loop.
 */
public final class ModuleManager {

    private static ModuleManager instance;

    private final List<Module> modules = new ArrayList<>();
    private final Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);
    private final Map<Module, Boolean> lastKeyState = new HashMap<>();

    private ModuleManager() {
        for (Category c : Category.values()) {
            byCategory.put(c, new ArrayList<>());
        }
        registerAll();
    }

    public static ModuleManager get() {
        if (instance == null) instance = new ModuleManager();
        return instance;
    }

    private void register(Module module) {
        modules.add(module);
        byCategory.get(module.getCategory()).add(module);

        // Every module gets a HUD layer slot. Modules that don't draw anything just keep
        // Module's default no-op render() (negligible cost), which keeps this registration
        // logic simple instead of hand-listing which modules are "visual".
        HudElementRegistry.addLast(
                Identifier.of("veltomc", module.getName().toLowerCase().replace(" ", "_")),
                module
        );
    }

    private void registerAll() {
        // Combat
        register(new AttackTimerModule());

        // Movement
        register(new ToggleSprintModule());
        register(new StaticFovModule());

        // Render
        register(new FullbrightModule());
        register(new ZoomModule());
        register(new CrosshairModule());

        // Player
        register(new ArmorHudModule());
        register(new PotionHudModule());
        register(new AutoRespawnModule());

        // HUD
        register(new FpsCounterModule());
        register(new CpsCounterModule());
        register(new PingModule());
        register(new CoordinatesModule());
        register(new KeystrokesModule());

        // Misc
        register(new ChatTimestampsModule());

        // Settings has no toggle-modules; it's a dedicated panel in the ClickGUI.
    }

    public List<Module> all() {
        return modules;
    }

    public List<Module> inCategory(Category category) {
        return byCategory.get(category);
    }

    public Module byName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    /** Called every client tick from VeltoMCClient. */
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Poll raw keybinds only while in-game (no screen open) so typing/GUI use isn't hijacked.
        if (client.currentScreen == null && client.getWindow() != null) {
            long handle = client.getWindow().getHandle();
            for (Module m : modules) {
                if (m.getKeyCode() <= 0) continue;
                boolean down = GLFW.glfwGetKey(handle, m.getKeyCode()) == GLFW.GLFW_PRESS;
                boolean wasDown = lastKeyState.getOrDefault(m, false);
                if (down && !wasDown) {
                    m.toggle();
                }
                lastKeyState.put(m, down);
            }
        }

        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }
}
