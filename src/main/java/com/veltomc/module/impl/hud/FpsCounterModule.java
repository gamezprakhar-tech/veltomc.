package com.veltomc.module.impl.hud;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class FpsCounterModule extends Module {

    public FpsCounterModule() {
        super("FPS Counter", "Shows current frames per second.", Category.HUD, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.options.hudHidden) return;
        String line = client.getCurrentFps() + " FPS";
        context.drawText(client.textRenderer, Text.literal(line), 6, 6, 0xFFFFFFFF, true);
    }
}
