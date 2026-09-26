package com.veltomc.module.impl.player;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

/** QoL: automatically clicks "Respawn" for you instead of making you click it. */
public class AutoRespawnModule extends Module {

    public AutoRespawnModule() {
        super("Auto Respawn", "Automatically respawns you when the death screen appears.", Category.PLAYER, false);
    }

    @Override
    public void onTick() {
        if (client.currentScreen instanceof DeathScreen && client.player != null) {
            client.player.requestRespawn();
        }
    }
}
