package com.veltomc.module.impl.hud;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class PingModule extends Module {

    public PingModule() {
        super("Ping", "Shows your connection latency in milliseconds.", Category.HUD, false);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled() || client.options.hudHidden || client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        int ping = entry != null ? entry.getLatency() : -1;
        String line = ping >= 0 ? (ping + " ms") : "-- ms";
        context.drawText(client.textRenderer, Text.literal(line), 6, 26, 0xFFFFFFFF, true);
    }
}
