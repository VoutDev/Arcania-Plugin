package me.vout.paper.arcania.enchant;

public enum EnchantExtraEnum {

    TOOL_TIP("#F6F6F7"),
    DESCRIPTION("#CCCCCC"),
    LEVEL_COST("#ACD0AE"),
    ERROR_MESSAGE("#F4564B");

    private final String color;
    EnchantExtraEnum(String color) { this.color = color; }
    public String getColor() { return color; }
}
