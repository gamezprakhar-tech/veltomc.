package com.veltomc.module.impl.hud;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class CoordinatesModule extends Module {

    public CoordinatesModule() {
        super("Coordinates", "Shows your current X/Y/Z position.", Category.HUD, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.options.hudHidden || client.player == null) return;
        String line = String.format("X: %.1f Y: %.1f Z: %.1f",
                client.player.getX(), client.player.getY(), client.player.getZ());
        context.drawText(client.textRenderer, Text.literal(line), 6, 36, 0xFFFFFFFF, true);
    }
}
