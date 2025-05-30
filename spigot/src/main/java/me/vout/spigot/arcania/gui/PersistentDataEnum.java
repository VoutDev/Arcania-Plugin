package me.vout.spigot.arcania.gui;

public enum PersistentDataEnum {
    BUTTON_TYPE("button_type"),
    GUI_REDIRECT("gui_redirect"),
    GUI_ACTION("gui_action"),
    ENCHANT("enchant"),
    FILTER("enchants_filter");

    private final String key;
    PersistentDataEnum(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}