package com.veltomc.module.impl.render;

import com.veltomc.module.Category;
import com.veltomc.module.Module;

/**
 * Toggle-to-zoom: lowers FOV while enabled, restores it when disabled. Pure camera
 * change - no effect on gameplay, hitboxes, or reach.
 */
public class ZoomModule extends Module {

    private double previousFov = -1;
    private boolean zooming = false;

    public ZoomModule() {
        super("Zoom", "Zooms the camera in while enabled.", Category.RENDER, false);
    }

    @Override
    protected void onEnable() {
        if (client.options == null) return;
        previousFov = client.options.getFov().getValue();
        client.options.getFov().setValue(30);
        zooming = true;
    }

    @Override
    protected void onDisable() {
        if (zooming && previousFov > 0 && client.options != null) {
            client.options.getFov().setValue((int) previousFov);
        }
        zooming = false;
    }
}
