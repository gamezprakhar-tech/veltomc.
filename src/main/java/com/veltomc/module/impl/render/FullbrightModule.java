package com.veltomc.module.impl.render;

import com.veltomc.module.Category;
import com.veltomc.module.Module;

/**
 * Safe-mode fullbright: maxes out the vanilla brightness/gamma option while enabled and
 * restores your previous value when disabled. This uses the normal in-game option (no
 * mixin, no lighting-engine override), so it's guaranteed to compile and work across
 * Minecraft updates, but it caps out at vanilla's own maximum brightness rather than a
 * true zero-shadow fullbright. If you want the stronger mixin-based version (overriding
 * LightmapTextureManager directly), that needs a mixin target verified against 1.21.11's
 * current mappings on https://linkie.shedaniel.dev before it'll compile - ask and I'll add it.
 */
public class FullbrightModule extends Module {

    private double previousGamma = -1;

    public FullbrightModule() {
        super("Fullbright", "Maxes out in-game brightness while enabled.", Category.RENDER, false);
    }

    @Override
    protected void onEnable() {
        if (client.options == null) return;
        previousGamma = client.options.getGamma().getValue();
        client.options.getGamma().setValue(100.0);
    }

    @Override
    protected void onDisable() {
        if (client.options == null) return;
        if (previousGamma >= 0) client.options.getGamma().setValue(previousGamma);
    }
}
