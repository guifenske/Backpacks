package br.com.backpacks.upgrades;


import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.backpack.BackpackAction;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum UpgradeType {
    AUTOFEED("Auto Feed", UpgradesRecipes.AUTOFEED, Material.COOKED_BEEF, BackpackAction.Action.UPGAUTOFEED),

    AUTOFILL("Auto Fill", UpgradesRecipes.AUTOFILL, Material.DISPENSER, BackpackAction.Action.NOTHING),

    COLLECTOR("Collector", UpgradesRecipes.COLLECTOR, Material.HOPPER, BackpackAction.Action.UPGAUTOFEED),

    CRAFTING_GRID("Crafting Grid", UpgradesRecipes.CRAFTING_GRID, Material.CRAFTING_TABLE, BackpackAction.Action.UPGCRAFTINGGRID),

    ENCAPSULATE("Encapsulate", UpgradesRecipes.ENCAPSULATE, Material.GLASS_BOTTLE, BackpackAction.Action.NOTHING),

    FURNACE("Furnace", UpgradesRecipes.FURNACE, Material.FURNACE, BackpackAction.Action.UPGFURNACE),

    JUKEBOX("Jukebox", UpgradesRecipes.JUKEBOX, Material.JUKEBOX, BackpackAction.Action.UPGJUKEBOX),

    LIQUID_TANK("Liquid Tank", UpgradesRecipes.LIQUID_TANK, Material.BUCKET, BackpackAction.Action.UPGTANKS),

    MAGNET("Magnet", UpgradesRecipes.MAGNET, Material.ENDER_EYE, BackpackAction.Action.NOTHING),

    UNBREAKABLE("Unbreakable", UpgradesRecipes.UNBREAKABLE, Material.ENCHANTED_GOLDEN_APPLE, BackpackAction.Action.NOTHING),

    VILLAGER_BAIT("Villager Bait", UpgradesRecipes.VILLAGER_BAIT, Material.EMERALD_BLOCK, BackpackAction.Action.UPGVILLAGERBAIT);

    UpgradeType(String name, NamespacedKey key, Material material, BackpackAction.Action action) {
        this.name = name;
        this.key = key;
        this.material = material;
        this.action = action;
    }

    private final String name;

    private final NamespacedKey key;

    private final Material material;

    private final BackpackAction.Action action;

    public String getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Material getMaterial() {
        return material;
    }

    public BackpackAction.Action getAction() {
        return action;
    }
}