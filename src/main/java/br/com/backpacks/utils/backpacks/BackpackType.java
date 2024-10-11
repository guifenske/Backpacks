package br.com.backpacks.utils.backpacks;

import br.com.backpacks.recipes.BackpackRecipes;
import org.bukkit.NamespacedKey;

public enum BackpackType {

    LEATHER ("Leather Backpack", BackpackRecipes.getNAMESPACE_LEATHER_BACKPACK()),

    IRON ("Iron Backpack", BackpackRecipes.getNAMESPACE_IRON_BACKPACK()),

    GOLD ("Gold Backpack", BackpackRecipes.getNAMESPACE_GOLD_BACKPACK()),

    LAPIS ("Lapis Backpack", BackpackRecipes.getNAMESPACE_LAPIS_BACKPACK()),

    AMETHYST ("Amethyst Backpack", BackpackRecipes.getNAMESPACE_AMETHYST_BACKPACK()),

    DIAMOND ("Diamond Backpack", BackpackRecipes.getNAMESPACE_DIAMOND_BACKPACK()),

    NETHERITE ("Netherite Backpack", BackpackRecipes.getNAMESPACE_NETHERITE_BACKPACK());

    BackpackType(String name, NamespacedKey key){
        this.name = name;
        this.key = key;
    }

    private final String name;

    private final NamespacedKey key;

    public String getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
