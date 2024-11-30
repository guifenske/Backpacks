package br.com.backpacks.backpack;

import br.com.backpacks.recipes.BackpackRecipes;
import org.bukkit.NamespacedKey;

public enum BackpackType {

    LEATHER ("Leather Backpack", BackpackRecipes.NAMESPACE_LEATHER_BACKPACK, 2, 18, 0),

    IRON ("Iron Backpack", BackpackRecipes.NAMESPACE_IRON_BACKPACK, 3, 27, 0),

    GOLD ("Gold Backpack", BackpackRecipes.NAMESPACE_GOLD_BACKPACK, 4, 36, 0),

    LAPIS ("Lapis Backpack", BackpackRecipes.NAMESPACE_LAPIS_BACKPACK, 5, 45, 0),

    AMETHYST ("Amethyst Backpack", BackpackRecipes.NAMESPACE_AMETHYST_BACKPACK, 6, 54, 0),

    DIAMOND ("Diamond Backpack", BackpackRecipes.NAMESPACE_DIAMOND_BACKPACK, 7, 54, 27),

    NETHERITE ("Netherite Backpack", BackpackRecipes.NAMESPACE_NETHERITE_BACKPACK, 9, 54, 54);

    BackpackType(String name, NamespacedKey key, int maxUpgrades, int firstPageSize, int secondPageSize){
        this.name = name;
        this.key = key;
        this.maxUpgrades = maxUpgrades;
        this.firstPageSize = firstPageSize;
        this.secondPageSize = secondPageSize;
    }

    private final String name;

    private final NamespacedKey key;

    private final int maxUpgrades;

    private final int firstPageSize;

    private final int secondPageSize;

    public String getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public int getMaxUpgrades() {
        return maxUpgrades;
    }

    public int getFirstPageSize() {
        return firstPageSize;
    }

    public int getSecondPageSize() {
        return secondPageSize;
    }
}
