package me.vout.core.arcania.enums;

public enum EnchantsFilterEnum {
    COMMON_FILTER("common_filter"),
    UNCOMMON_FILTER("uncommon_filter"),
    RARE_FILTER("rare_filter"),
    LEGENDARY_FILTER("legendary_filter"),
    ULTRA_FILTER("ultra_filter"),
    ALL("all");

    private final String key;

    EnchantsFilterEnum(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
