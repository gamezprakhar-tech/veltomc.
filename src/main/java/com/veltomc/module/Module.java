package com.veltomc.module;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Base class for every VELTOMC feature. A Module is a simple on/off toggle that can
 * optionally draw something to the HUD and/or react to client ticks. There is no
 * combat-assist logic anywhere in this class or its subclasses (no targeting, no
 * automated clicking, no packet manipulation) - purely visual/QoL helpers.
 */
public abstract class Module implements HudElement {

    protected final MinecraftClient client = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int keyCode = -1; // GLFW key code, -1 = unbound

    protected Module(String name, String description, Category category, boolean enabledByDefault) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = enabledByDefault;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public void setEnabled(boolean value) {
        if (this.enabled == value) return;
        this.enabled = value;
        if (value) onEnable();
        else onDisable();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    /** Called once when the module is turned on. */
    protected void onEnable() {}

    /** Called once when the module is turned off. */
    protected void onDisable() {}

    /** Called every client tick, regardless of whether it renders anything. */
    public void onTick() {}

    /**
     * HudElement render hook (Fabric API's HUD layer system). Only modules that draw
     * something to the screen (HUD counters, crosshair, keystrokes, etc.) override this;
     * everything else can leave it empty.
     */
    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {}
}
