package me.vout.paper.arcania.gui;

public enum GuiTypeEnum {
    MAIN("Arcania Enchants", 27);


    private final String displayName;
    private final Integer staticSize; // null if dynamic

    GuiTypeEnum(String displayName, Integer staticSize) {
        this.displayName = displayName;
        this.staticSize = staticSize;
    }

    GuiTypeEnum(String displayName) {
        this(displayName, null);
    }

    public String getDisplayName() { return displayName; }
    public Integer getStaticSize() { return staticSize; }
    public boolean isStaticSize() { return staticSize != null; }
}

