package com.veltomc.module.impl.hud;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/** Classic WASD + mouse-button keystrokes display, bottom-right of the screen. */
public class KeystrokesModule extends Module {

    private static final int BOX = 20;
    private static final int GAP = 2;

    public KeystrokesModule() {
        super("Keystrokes", "Shows WASD and mouse-button presses.", Category.HUD, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.options.hudHidden) return;

        int originX = context.getScaledWindowWidth() - (BOX * 3 + GAP * 2) - 10;
        int originY = context.getScaledWindowHeight() - (BOX * 3 + GAP * 2) - 10;

        drawKey(context, originX + BOX + GAP, originY, "W", client.options.forwardKey.isPressed());
        drawKey(context, originX, originY + BOX + GAP, "A", client.options.leftKey.isPressed());
        drawKey(context, originX + BOX + GAP, originY + BOX + GAP, "S", client.options.backKey.isPressed());
        drawKey(context, originX + (BOX + GAP) * 2, originY + BOX + GAP, "D", client.options.rightKey.isPressed());

        int rowY = originY + (BOX + GAP) * 2 + 4;
        drawWideKey(context, originX, rowY, BOX, "LMB", ClickTracker.isLeftDown());
        drawWideKey(context, originX + BOX + GAP, rowY, BOX, "JMP", client.options.jumpKey.isPressed());
        drawWideKey(context, originX + (BOX + GAP) * 2, rowY, BOX, "RMB", ClickTracker.isRightDown());
    }

    private void drawKey(DrawContext context, int x, int y, String label, boolean pressed) {
        drawWideKey(context, x, y, BOX, label, pressed);
    }

    private void drawWideKey(DrawContext context, int x, int y, int size, String label, boolean pressed) {
        int bg = pressed ? 0xCC7C4DFF : 0x99202020;
        context.fill(x, y, x + size, y + size, bg);
        int textWidth = client.textRenderer.getWidth(label);
        context.drawText(client.textRenderer, label, x + (size - textWidth) / 2, y + (size - 8) / 2, 0xFFFFFFFF, false);
    }
}
