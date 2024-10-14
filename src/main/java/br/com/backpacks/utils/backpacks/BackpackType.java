package br.com.backpacks.utils.backpacks;

import br.com.backpacks.recipes.BackpackRecipes;
import org.bukkit.NamespacedKey;

public enum BackpackType {

    LEATHER ("Leather Backpack", BackpackRecipes.NAMESPACE_LEATHER_BACKPACK),

    IRON ("Iron Backpack", BackpackRecipes.NAMESPACE_IRON_BACKPACK),

    GOLD ("Gold Backpack", BackpackRecipes.NAMESPACE_GOLD_BACKPACK),

    LAPIS ("Lapis Backpack", BackpackRecipes.NAMESPACE_LAPIS_BACKPACK),

    AMETHYST ("Amethyst Backpack", BackpackRecipes.NAMESPACE_AMETHYST_BACKPACK),

    DIAMOND ("Diamond Backpack", BackpackRecipes.NAMESPACE_DIAMOND_BACKPACK),

    NETHERITE ("Netherite Backpack", BackpackRecipes.NAMESPACE_NETHERITE_BACKPACK);

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
