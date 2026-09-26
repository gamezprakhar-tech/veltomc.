package com.veltomc.module.impl.combat;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Draws a small horizontal bar under the crosshair showing attack-cooldown progress
 * (0-100%). Purely a readability aid - it does not swing your arm, click for you, or
 * change cooldown timing in any way.
 */
public class AttackTimerModule extends Module {

    public AttackTimerModule() {
        super("Attack Timer", "Shows your weapon cooldown as a bar under the crosshair.", Category.COMBAT, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.player == null || client.options.hudHidden) return;

        PlayerEntity player = client.player;
        float progress = player.getAttackCooldownProgress(0.0f);
        if (progress >= 1.0f) return; // fully ready, nothing useful to show

        int screenW = context.getScaledWindowWidth();
        int screenH = context.getScaledWindowHeight();

        int barWidth = 40;
        int barHeight = 3;
        int x = screenW / 2 - barWidth / 2;
        int y = screenH / 2 + 12;

        context.fill(x, y, x + barWidth, y + barHeight, 0x99000000);
        int filled = (int) (barWidth * progress);
        int color = progress < 1.0f ? 0xFF7C4DFF : 0xFF4CFF7C;
        context.fill(x, y, x + filled, y + barHeight, color);
    }
}
