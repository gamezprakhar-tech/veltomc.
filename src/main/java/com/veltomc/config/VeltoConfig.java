package com.veltomc.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Plain data object serialized to config/veltomc.json. Gson fills this in directly,
 * so field names matter - don't rename without a migration plan.
 */
public class VeltoConfig {

    // moduleName -> enabled
    public Map<String, Boolean> enabledModules = new HashMap<>();

    // moduleName -> bound GLFW key code
    public Map<String, Integer> keybinds = new HashMap<>();

    // Global GUI look & feel, edited from the Settings tab
    public GuiSettings gui = new GuiSettings();

    // Crosshair-specific settings, edited from the Render tab
    public CrosshairSettings crosshair = new CrosshairSettings();

    public static class GuiSettings {
        public float scale = 1.0f;
        public int accentColorArgb = 0xFF7C4DFF; // purple accent, VELTOMC default
        public boolean backgroundBlur = true;
        public int clickGuiKey = 344; // GLFW_KEY_RIGHT_SHIFT
    }

    public static class CrosshairSettings {
        public boolean enabled = false;
        public int size = 6;
        public int thickness = 1;
        public int colorArgb = 0xFFFFFFFF;
        public String style = "cross"; // "cross" or "dot"
    }
}
