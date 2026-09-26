package com.veltomc.module.impl.movement;

import com.veltomc.module.Category;
import com.veltomc.module.Module;

/**
 * While enabled, keeps you sprinting whenever you're moving forward - same effect as
 * vanilla's "Auto sprint" accessibility option, just as a toggleable module. No speed
 * bonus beyond vanilla sprint, no bypass of any server-side sprint checks.
 */
public class ToggleSprintModule extends Module {

    public ToggleSprintModule() {
        super("Toggle Sprint", "Automatically sprints while you're moving forward.", Category.MOVEMENT, false);
    }

    @Override
    public void onTick() {
        if (client.player == null) return;
        boolean movingForward = client.options.forwardKey.isPressed();
        if (movingForward && !client.player.isSneaking() && client.player.getHungerManager().getFoodLevel() > 6) {
            client.player.setSprinting(true);
        }
    }
}
