package com.veltomc.gui;

import net.minecraft.client.util.InputUtil;

/** Small helper to turn a GLFW key code into a human-readable label for the GUI. */
public final class GuiKeys {
    private GuiKeys() {}

    public static String name(int keyCode) {
        if (keyCode <= 0) return "unbound";
        try {
            return InputUtil.fromKeyCode(keyCode, -1).getLocalizedText().getString();
        } catch (Exception e) {
            return "key " + keyCode;
        }
    }
}
