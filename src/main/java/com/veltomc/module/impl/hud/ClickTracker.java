package com.veltomc.module.impl.hud;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Tracks raw mouse-button press timestamps so CPS/Keystrokes can show real click rate.
 * Fed by com.veltomc.mixin.MouseMixin, which hooks the raw input callback (this is the
 * only mixin in the whole mod - counting clicks isn't exposed by any Fabric API event).
 */
public final class ClickTracker {

    private static final Deque<Long> leftClicks = new ArrayDeque<>();
    private static final Deque<Long> rightClicks = new ArrayDeque<>();

    private ClickTracker() {}

    private static volatile boolean leftDown = false;
    private static volatile boolean rightDown = false;

    public static void setLeftDown(boolean down) {
        leftDown = down;
    }

    public static void setRightDown(boolean down) {
        rightDown = down;
    }

    public static boolean isLeftDown() {
        return leftDown;
    }

    public static boolean isRightDown() {
        return rightDown;
    }

    public static void onLeftClick() {
        leftClicks.addLast(System.currentTimeMillis());
    }

    public static void onRightClick() {
        rightClicks.addLast(System.currentTimeMillis());
    }

    public static int leftCps() {
        return countRecent(leftClicks);
    }

    public static int rightCps() {
        return countRecent(rightClicks);
    }

    public static int totalCps() {
        return leftCps() + rightCps();
    }

    private static int countRecent(Deque<Long> deque) {
        long now = System.currentTimeMillis();
        while (!deque.isEmpty() && now - deque.peekFirst() > 1000) {
            deque.pollFirst();
        }
        return deque.size();
    }
}
