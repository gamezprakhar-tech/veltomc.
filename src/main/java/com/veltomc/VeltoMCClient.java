package com.veltomc;

import com.veltomc.config.ConfigManager;
import com.veltomc.gui.ClickGuiScreen;
import com.veltomc.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/**
 * VELTOMC client entrypoint. This mod is client-only: it never touches the server,
 * never sends extra packets, and does not include any auto-play/assist logic. It is a
 * ClickGUI wrapper around cosmetic and QoL toggles only.
 */
public class VeltoMCClient implements ClientModInitializer {

    private boolean guiKeyWasDown = false;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::save));
    }

    private void onTick(MinecraftClient client) {
        ModuleManager.get().tick();
        pollGuiKey(client);
    }

    private void pollGuiKey(MinecraftClient client) {
        if (client.getWindow() == null) return;
        int keyCode = ConfigManager.get().gui.clickGuiKey;
        if (keyCode <= 0) return;

        long handle = client.getWindow().getHandle();
        boolean down = GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;

        // Don't hijack the key while the player is typing in chat/sign/book/etc.
        boolean typing = client.currentScreen != null && !(client.currentScreen instanceof ClickGuiScreen);

        if (down && !guiKeyWasDown && !typing) {
            if (client.currentScreen instanceof ClickGuiScreen) {
                client.setScreen(null);
            } else if (client.currentScreen == null) {
                client.setScreen(new ClickGuiScreen());
            }
        }
        guiKeyWasDown = down;
    }
}
