package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class UpgradesRecipesNamespaces {

    private NamespacedKey TRASHCAN = new NamespacedKey(Main.getMain(), "trashcan");

    private NamespacedKey NAMESPACEISUPGRADE = new NamespacedKey(Main.getMain(), "isupgrade");

    public NamespacedKey isUpgrade() {
        return NAMESPACEISUPGRADE;
    }

    public NamespacedKey getTRASHCAN() {
        return TRASHCAN;
    }

    public Recipe getRecipeTrashCan() {
        ItemStack trashCan = new ItemStack(Material.CAULDRON);
        ItemMeta meta = trashCan.getItemMeta();

        meta.setDisplayName("Trash Can");
        meta.setLore(Arrays.asList("§7Trash Can", "§7§nRemoves one slot from the backpack, to be used ", "§7§nas an trash slot."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(TRASHCAN, PersistentDataType.INTEGER, 1);
        trashCan.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(TRASHCAN, trashCan);

        recipe.shape(" I ", "ILI", " I ");

        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('L', Material.LAVA_BUCKET);;
        return recipe;
    }
}