package com.veltomc.module;

/**
 * The seven ClickGUI tabs. Order here is the order they're drawn in the sidebar.
 */
public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    HUD("HUD"),
    MISC("Misc"),
    SETTINGS("Settings");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
