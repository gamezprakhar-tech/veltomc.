package com.veltomc.mixin;

import com.veltomc.module.impl.hud.ClickTracker;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Only mixin in the mod. Hooks raw mouse-button events purely to count clicks/second
 * for the CPS + Keystrokes HUD modules. Does not alter, cancel, or inject any input -
 * it only observes.
 *
 * If this fails to compile against 1.21.11, check the current method name/signature for
 * this class via https://linkie.shedaniel.dev (search "onMouseButton" under Yarn) - the
 * class has historically been net.minecraft.client.Mouse#onMouseButton(long,int,int,int).
 */
@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void veltomc$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        boolean pressed = action != GLFW.GLFW_RELEASE;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            ClickTracker.setLeftDown(pressed);
            if (action == GLFW.GLFW_PRESS) ClickTracker.onLeftClick();
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            ClickTracker.setRightDown(pressed);
            if (action == GLFW.GLFW_PRESS) ClickTracker.onRightClick();
        }
    }
}
