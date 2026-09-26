package com.veltomc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veltomc.module.Module;
import com.veltomc.module.ModuleManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and saves config/veltomc.json. Fabric Loader's config dir already gives us a
 * per-instance location, and Gson ships bundled with Minecraft so no extra dependency
 * is needed.
 */
public final class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("veltomc.json");

    private static VeltoConfig current = new VeltoConfig();

    private ConfigManager() {}

    public static VeltoConfig get() {
        return current;
    }

    public static void load() {
        if (!Files.exists(PATH)) {
            current = new VeltoConfig();
            return;
        }
        try (Reader reader = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
            VeltoConfig loaded = GSON.fromJson(reader, VeltoConfig.class);
            current = (loaded != null) ? loaded : new VeltoConfig();
        } catch (IOException e) {
            current = new VeltoConfig();
        }
        applyToModules();
    }

    public static void save() {
        captureFromModules();
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(current, writer);
            }
        } catch (IOException ignored) {
            // Non-fatal - worst case, settings just don't persist this session.
        }
    }

    /** Push saved enabled-states/keybinds onto the live module instances. */
    private static void applyToModules() {
        for (Module m : ModuleManager.get().all()) {
            Boolean enabled = current.enabledModules.get(m.getName());
            if (enabled != null) m.setEnabled(enabled);

            Integer key = current.keybinds.get(m.getName());
            if (key != null) m.setKeyCode(key);
        }
    }

    /** Pull current live module state into the config object before writing it out. */
    private static void captureFromModules() {
        for (Module m : ModuleManager.get().all()) {
            current.enabledModules.put(m.getName(), m.isEnabled());
            current.keybinds.put(m.getName(), m.getKeyCode());
        }
    }
}
