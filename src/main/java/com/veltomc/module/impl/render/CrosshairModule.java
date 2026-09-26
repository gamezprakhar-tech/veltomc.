package com.veltomc.module.impl.render;

import com.veltomc.config.ConfigManager;
import com.veltomc.config.VeltoConfig;
import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/** Draws a custom crosshair (cross or dot) in place of / on top of the vanilla one. */
public class CrosshairModule extends Module {

    public CrosshairModule() {
        super("Custom Crosshair", "Replaces the crosshair with a customizable one.", Category.RENDER, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.options.hudHidden || client.player == null) return;

        VeltoConfig.CrosshairSettings cfg = ConfigManager.get().crosshair;
        int cx = context.getScaledWindowWidth() / 2;
        int cy = context.getScaledWindowHeight() / 2;
        int size = cfg.size;
        int thickness = Math.max(1, cfg.thickness);
        int color = cfg.colorArgb;

        if ("dot".equalsIgnoreCase(cfg.style)) {
            context.fill(cx - thickness, cy - thickness, cx + thickness, cy + thickness, color);
            return;
        }

        // cross
        context.fill(cx - size, cy - thickness / 2, cx + size, cy + thickness / 2 + thickness, color);
        context.fill(cx - thickness / 2, cy - size, cx + thickness / 2 + thickness, cy + size, color);
    }
}
