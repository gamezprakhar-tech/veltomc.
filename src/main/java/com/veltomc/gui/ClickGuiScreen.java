package com.veltomc.gui;

import com.veltomc.config.ConfigManager;
import com.veltomc.config.VeltoConfig;
import com.veltomc.module.Category;
import com.veltomc.module.Module;
import com.veltomc.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * VELTOMC's ClickGUI. Dark background, purple accent, a category sidebar on the left
 * and a scrolling module list on the right. Opened/closed with Right Shift (rebindable
 * from the Settings tab).
 */
public class ClickGuiScreen extends Screen {

    private static final int PANEL_W = 420;
    private static final int PANEL_H = 260;
    private static final int SIDEBAR_W = 110;
    private static final int ROW_H = 22;

    private static final int COLOR_BG = 0xE6141414;
    private static final int COLOR_SIDEBAR = 0xF01B1B1F;
    private static final int COLOR_ROW = 0x33FFFFFF;
    private static final int COLOR_ROW_HOVER = 0x55FFFFFF;
    private static final int COLOR_TEXT = 0xFFE6E6E6;
    private static final int COLOR_TEXT_DIM = 0xFF9A9A9A;

    private Category selected = Category.COMBAT;
    private Module capturingKeybindFor = null;

    private int panelX, panelY;

    public ClickGuiScreen() {
        super(Text.literal("VELTOMC"));
    }

    @Override
    protected void init() {
        panelX = (width - PANEL_W) / 2;
        panelY = (height - PANEL_H) / 2;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dim the world behind the GUI slightly instead of the vanilla blur/darken.
        context.fill(0, 0, width, height, 0x66000000);

        int accent = ConfigManager.get().gui.accentColorArgb;

        // Main panel
        context.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, COLOR_BG);
        context.fill(panelX, panelY, panelX + PANEL_W, panelY + 2, accent);

        // Title
        context.drawText(textRenderer, Text.literal("VELTOMC"), panelX + 10, panelY - 14, 0xFFFFFFFF, true);

        // Sidebar
        context.fill(panelX, panelY, panelX + SIDEBAR_W, panelY + PANEL_H, COLOR_SIDEBAR);
        int tabY = panelY + 8;
        for (Category cat : Category.values()) {
            boolean hovered = mouseX >= panelX && mouseX <= panelX + SIDEBAR_W && mouseY >= tabY && mouseY <= tabY + 20;
            boolean active = cat == selected;
            if (active) {
                context.fill(panelX, tabY, panelX + SIDEBAR_W, tabY + 20, accent & 0x66FFFFFF | 0x33000000);
                context.fill(panelX, tabY, panelX + 3, tabY + 20, accent);
            } else if (hovered) {
                context.fill(panelX, tabY, panelX + SIDEBAR_W, tabY + 20, COLOR_ROW);
            }
            context.drawText(textRenderer, Text.literal(cat.getDisplayName()), panelX + 10, tabY + 6,
                    active ? 0xFFFFFFFF : COLOR_TEXT_DIM, false);
            tabY += 22;
        }

        // Content
        int contentX = panelX + SIDEBAR_W + 8;
        int contentY = panelY + 8;
        int contentW = PANEL_W - SIDEBAR_W - 16;

        if (selected == Category.SETTINGS) {
            renderSettingsPanel(context, contentX, contentY, contentW, mouseX, mouseY);
        } else {
            renderModuleList(context, contentX, contentY, contentW, mouseX, mouseY);
        }

        if (capturingKeybindFor != null) {
            String prompt = "Press a key to bind \"" + capturingKeybindFor.getName() + "\" (ESC to cancel)";
            context.drawText(textRenderer, Text.literal(prompt), panelX + 10, panelY + PANEL_H + 6, 0xFFFFD966, true);
        }
    }

    private void renderModuleList(DrawContext context, int x, int y, int w, int mouseX, int mouseY) {
        List<Module> modules = ModuleManager.get().inCategory(selected);
        int rowY = y;
        for (Module m : modules) {
            boolean hovered = mouseX >= x && mouseX <= x + w && mouseY >= rowY && mouseY <= rowY + ROW_H - 2;
            context.fill(x, rowY, x + w, rowY + ROW_H - 2, hovered ? COLOR_ROW_HOVER : COLOR_ROW);

            int dotColor = m.isEnabled() ? 0xFF4CFF7C : 0xFF6B6B6B;
            context.fill(x + 6, rowY + 7, x + 10, rowY + 11, dotColor);

            context.drawText(textRenderer, Text.literal(m.getName()), x + 16, rowY + 6, COLOR_TEXT, false);

            String bind = m.getKeyCode() > 0 ? GuiKeys.name(m.getKeyCode()) : "unbound";
            int bindWidth = textRenderer.getWidth(bind);
            context.drawText(textRenderer, Text.literal(bind), x + w - bindWidth - 6, rowY + 6, COLOR_TEXT_DIM, false);

            rowY += ROW_H;
        }

        if (modules.isEmpty()) {
            context.drawText(textRenderer, Text.literal("No modules in this category."), x, y, COLOR_TEXT_DIM, false);
        }
    }

    private void renderSettingsPanel(DrawContext context, int x, int y, int w, int mouseX, int mouseY) {
        VeltoConfig cfg = ConfigManager.get();

        context.drawText(textRenderer, Text.literal("ClickGUI keybind"), x, y, COLOR_TEXT, false);
        String bindLabel = GuiKeys.name(cfg.gui.clickGuiKey);
        drawButton(context, x, y + 12, w, "Rebind: " + bindLabel, mouseX, mouseY, "rebind_gui");

        context.drawText(textRenderer, Text.literal("Accent color"), x, y + 40, COLOR_TEXT, false);
        int[] swatches = {0xFF7C4DFF, 0xFF4CC2FF, 0xFF4CFF7C, 0xFFFF4C6B, 0xFFFFC24C};
        int sx = x;
        for (int c : swatches) {
            context.fill(sx, y + 52, sx + 16, y + 68, c);
            if (c == cfg.gui.accentColorArgb) {
                context.drawBorder(sx - 1, y + 51, 18, 18, 0xFFFFFFFF);
            }
            sx += 20;
        }

        context.drawText(textRenderer, Text.literal("Background blur: " + (cfg.gui.backgroundBlur ? "On" : "Off")),
                x, y + 82, COLOR_TEXT, false);
        drawButton(context, x, y + 94, w, "Toggle", mouseX, mouseY, "toggle_blur");

        context.drawText(textRenderer, Text.literal("Custom crosshair style: " + cfg.crosshair.style), x, y + 120, COLOR_TEXT, false);
        drawButton(context, x, y + 132, w / 2 - 4, "Cross", mouseX, mouseY, "crosshair_cross");
        drawButton(context, x + w / 2 + 4, y + 132, w / 2 - 4, "Dot", mouseX, mouseY, "crosshair_dot");

        drawButton(context, x, y + 168, w / 2 - 4, "Save config", mouseX, mouseY, "save");
        drawButton(context, x + w / 2 + 4, y + 168, w / 2 - 4, "Load config", mouseX, mouseY, "load");
    }

    private void drawButton(DrawContext context, int x, int y, int w, String label, int mouseX, int mouseY, String id) {
        boolean hovered = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + 16;
        context.fill(x, y, x + w, y + 16, hovered ? 0x66FFFFFF : 0x33FFFFFF);
        int tw = textRenderer.getWidth(label);
        context.drawText(textRenderer, Text.literal(label), x + (w - tw) / 2, y + 4, 0xFFFFFFFF, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            // Sidebar tabs
            int tabY = panelY + 8;
            for (Category cat : Category.values()) {
                if (mouseX >= panelX && mouseX <= panelX + SIDEBAR_W && mouseY >= tabY && mouseY <= tabY + 20) {
                    selected = cat;
                    return true;
                }
                tabY += 22;
            }

            int contentX = panelX + SIDEBAR_W + 8;
            int contentY = panelY + 8;
            int contentW = PANEL_W - SIDEBAR_W - 16;

            if (selected == Category.SETTINGS) {
                if (handleSettingsClick(contentX, contentY, contentW, mouseX, mouseY)) return true;
            } else {
                List<Module> modules = ModuleManager.get().inCategory(selected);
                int rowY = contentY;
                for (Module m : modules) {
                    if (mouseX >= contentX && mouseX <= contentX + contentW && mouseY >= rowY && mouseY <= rowY + ROW_H - 2) {
                        m.toggle();
                        return true;
                    }
                    rowY += ROW_H;
                }
            }
        } else if (button == 1 && selected != Category.SETTINGS) {
            // Right-click a row to (re)bind its key
            int contentX = panelX + SIDEBAR_W + 8;
            int contentY = panelY + 8;
            int contentW = PANEL_W - SIDEBAR_W - 16;
            List<Module> modules = ModuleManager.get().inCategory(selected);
            int rowY = contentY;
            for (Module m : modules) {
                if (mouseX >= contentX && mouseX <= contentX + contentW && mouseY >= rowY && mouseY <= rowY + ROW_H - 2) {
                    capturingKeybindFor = m;
                    return true;
                }
                rowY += ROW_H;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleSettingsClick(int x, int y, int w, double mouseX, double mouseY) {
        VeltoConfig cfg = ConfigManager.get();

        if (within(x, y + 12, w, 16, mouseX, mouseY)) {
            capturingSettingsGuiKey = true;
            return true;
        }
        int[] swatches = {0xFF7C4DFF, 0xFF4CC2FF, 0xFF4CFF7C, 0xFFFF4C6B, 0xFFFFC24C};
        int sx = x;
        for (int c : swatches) {
            if (within(sx, y + 52, 16, 16, mouseX, mouseY)) {
                cfg.gui.accentColorArgb = c;
                return true;
            }
            sx += 20;
        }
        if (within(x, y + 94, w, 16, mouseX, mouseY)) {
            cfg.gui.backgroundBlur = !cfg.gui.backgroundBlur;
            return true;
        }
        if (within(x, y + 132, w / 2 - 4, 16, mouseX, mouseY)) {
            cfg.crosshair.style = "cross";
            return true;
        }
        if (within(x + w / 2 + 4, y + 132, w / 2 - 4, 16, mouseX, mouseY)) {
            cfg.crosshair.style = "dot";
            return true;
        }
        if (within(x, y + 168, w / 2 - 4, 16, mouseX, mouseY)) {
            ConfigManager.save();
            return true;
        }
        if (within(x + w / 2 + 4, y + 168, w / 2 - 4, 16, mouseX, mouseY)) {
            ConfigManager.load();
            return true;
        }
        return false;
    }

    private boolean capturingSettingsGuiKey = false;

    private boolean within(int x, int y, int w, int h, double mx, double my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (capturingKeybindFor != null) {
            if (keyCode != 256) { // not ESC
                capturingKeybindFor.setKeyCode(keyCode);
            }
            capturingKeybindFor = null;
            return true;
        }
        if (capturingSettingsGuiKey) {
            if (keyCode != 256) {
                ConfigManager.get().gui.clickGuiKey = keyCode;
            }
            capturingSettingsGuiKey = false;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }
}
