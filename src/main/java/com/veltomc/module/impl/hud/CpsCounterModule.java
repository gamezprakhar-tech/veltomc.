package com.veltomc.module.impl.hud;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class CpsCounterModule extends Module {

    public CpsCounterModule() {
        super("CPS Counter", "Shows left/right mouse clicks per second.", Category.HUD, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.options.hudHidden) return;
        String line = "CPS " + ClickTracker.leftCps() + " / " + ClickTracker.rightCps();
        context.drawText(client.textRenderer, Text.literal(line), 6, 16, 0xFFFFFFFF, true);
    }
}
