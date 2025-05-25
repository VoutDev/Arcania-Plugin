package me.vout.arcania.enchant;

public enum EnchantExtraEnum {

    TOOL_TIP("#F6F6F7"),
    LEVEL_COST("#ACD0AE"),
    ERROR_MESSAGE("#F4564B");

    private final String color;
    EnchantExtraEnum(String color) { this.color = color; }
    public String getColor() { return color; }
}
