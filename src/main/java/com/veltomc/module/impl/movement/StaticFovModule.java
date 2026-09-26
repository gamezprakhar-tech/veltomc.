package com.veltomc.module.impl.movement;

import com.veltomc.module.Category;
import com.veltomc.module.Module;

/**
 * Purely cosmetic: stops the FOV from widening while sprinting/using an elytra so the
 * view feels more consistent. Does not touch movement speed, hitboxes, or reach.
 * Implemented in {@code MouseMixin}-free fashion by simply leaving the vanilla FOV
 * option value alone and letting ZoomModule/this module manage a single override flag
 * that the render code reads (see FullbrightModule/ZoomModule pattern in Render).
 */
public class StaticFovModule extends Module {

    public static volatile boolean ACTIVE = false;

    public StaticFovModule() {
        super("Static FOV", "Disables the FOV change from sprinting or elytra flight.", Category.MOVEMENT, false);
    }

    @Override
    protected void onEnable() {
        ACTIVE = true;
    }

    @Override
    protected void onDisable() {
        ACTIVE = false;
    }
}
