package br.com.backpacks.utils;


public enum UpgradeType {
    FURNACE("Furnace"),

    CRAFTING("Crafting"),

    JUKEBOX("Jukebox"),

    VILLAGERSFOLLOW("Villagers Follow"),

    COLLECTOR("Collector"),

    ENCAPSULATE("Encapsulate"),

    AUTOFILL("Auto Fill"),

    AUTOFEED("Auto Feed"),

    LIQUIDTANK("Liquid Tank"),

    UNBREAKABLE("Unbreakable"),

    FILTER("Filter"),

    ADVANCED_FILTER("Advanced Filter"),

    MAGNET("Magnet"),

    EXP_TANK("Exp Tank");

    final String name;
    public String getName(){
        return name;
    }

    UpgradeType(String name){
        this.name = name;
    }
}