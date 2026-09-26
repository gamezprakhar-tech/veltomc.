package com.veltomc.module.impl.player;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;

/** Lists your active potion/status effects with remaining duration, top-right corner. */
public class PotionHudModule extends Module {

    public PotionHudModule() {
        super("Potion HUD", "Lists active status effects and their duration.", Category.PLAYER, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.player == null || client.options.hudHidden) return;

        int x = context.getScaledWindowWidth() - 6;
        int y = 6;
        int lineHeight = 11;

        for (StatusEffectInstance effect : client.player.getStatusEffects()) {
            String name = effect.getEffectType().value().getName().getString();
            int seconds = effect.getDuration() / 20;
            String time = String.format("%d:%02d", seconds / 60, seconds % 60);
            String line = name + " " + (effect.getAmplifier() + 1) + " - " + time;

            int width = client.textRenderer.getWidth(line);
            context.fill(x - width - 6, y - 2, x, y + 9, 0x66000000);
            context.drawText(client.textRenderer, Text.literal(line), x - width - 3, y, 0xFFFFFFFF, false);
            y += lineHeight;
        }
    }
}
