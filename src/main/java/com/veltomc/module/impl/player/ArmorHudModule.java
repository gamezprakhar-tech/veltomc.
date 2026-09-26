package com.veltomc.module.impl.player;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;

/** Shows your four armor pieces as icons above the hotbar. */
public class ArmorHudModule extends Module {

    public ArmorHudModule() {
        super("Armor HUD", "Shows equipped armor pieces above the hotbar.", Category.PLAYER, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.player == null || client.options.hudHidden) return;

        int screenW = context.getScaledWindowWidth();
        int screenH = context.getScaledWindowHeight();
        int slotSize = 18;
        int totalWidth = slotSize * 4;
        int x = screenW / 2 - totalWidth / 2;
        int y = screenH - 68; // just above the hotbar/XP bar

        int i = 0;
        for (ItemStack stack : client.player.getArmorItems()) {
            int slotX = x + i * slotSize;
            context.fill(slotX, y, slotX + 16, y + 16, 0x66000000);
            if (!stack.isEmpty()) {
                context.drawItem(stack, slotX, y);
            }
            i++;
        }
    }
}
