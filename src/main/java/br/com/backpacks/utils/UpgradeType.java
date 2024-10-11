package br.com.backpacks.utils;


import br.com.backpacks.recipes.UpgradesRecipes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum UpgradeType {
    AUTOFEED("Auto Feed", UpgradesRecipes.AUTOFEED, Material.COOKED_BEEF),

    AUTOFILL("Auto Fill", UpgradesRecipes.AUTOFILL, Material.DISPENSER),

    COLLECTOR("Collector", UpgradesRecipes.COLLECTOR, Material.HOPPER),

    CRAFTING_GRID("Crafting Grid", UpgradesRecipes.CRAFTING_GRID, Material.CRAFTING_TABLE),

    ENCAPSULATE("Encapsulate", UpgradesRecipes.ENCAPSULATE, Material.GLASS_BOTTLE),

    FURNACE("Furnace", UpgradesRecipes.FURNACE, Material.FURNACE),

    JUKEBOX("Jukebox", UpgradesRecipes.JUKEBOX, Material.JUKEBOX),

    LIQUID_TANK("Liquid Tank", UpgradesRecipes.LIQUID_TANK, Material.BUCKET),

    MAGNET("Magnet", UpgradesRecipes.MAGNET, Material.ENDER_EYE),

    UNBREAKABLE("Unbreakable", UpgradesRecipes.UNBREAKABLE, Material.ENCHANTED_GOLDEN_APPLE),

    VILLAGER_BAIT("Villager Bait", UpgradesRecipes.VILLAGER_BAIT, Material.EMERALD_BLOCK);

    UpgradeType(String name, NamespacedKey key, Material material) {
        this.name = name;
        this.key = key;
        this.material = material;
    }

    private final String name;

    private final NamespacedKey key;

    private final Material material;

    public String getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Material getMaterial() {
        return material;
    }
}