package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class BackpackRecipes {
    public static final NamespacedKey IS_BACKPACK = new NamespacedKey(Main.getMain(), "isbackpack");
    public static final NamespacedKey BACKPACK_ID = new NamespacedKey(Main.getMain(), "backpackid");
    public static final NamespacedKey HAS_BACKPACK = new NamespacedKey(Main.getMain(), "hasbackpack");
    public static final NamespacedKey IS_CONFIG_ITEM = new NamespacedKey(Main.getMain(), "isconfigitem");
    public static final NamespacedKey NAMESPACE_LEATHER_BACKPACK = new NamespacedKey(Main.getMain(), "leatherbackpack");
    public static final NamespacedKey NAMESPACE_IRON_BACKPACK = new NamespacedKey(Main.getMain(), "ironbackpack");
    public static final NamespacedKey NAMESPACE_GOLD_BACKPACK = new NamespacedKey(Main.getMain(), "goldbackpack");
    public static final NamespacedKey NAMESPACE_LAPIS_BACKPACK = new NamespacedKey(Main.getMain(), "lapisbackpack");
    public static final NamespacedKey NAMESPACE_AMETHYST_BACKPACK = new NamespacedKey(Main.getMain(), "amethystbackpack");
    public static final NamespacedKey NAMESPACE_DRIED_BACKPACK = new NamespacedKey(Main.getMain(), "driedbackpack");
    public static final NamespacedKey NAMESPACE_WET_BACKPACK = new NamespacedKey(Main.getMain(), "wetbackpack");
    public static final NamespacedKey NAMESPACE_DIAMOND_BACKPACK = new NamespacedKey(Main.getMain(), "diamondbackpack");
    public static final NamespacedKey NAMESPACE_NETHERITE_BACKPACK = new NamespacedKey(Main.getMain(), "netheritebackpack");

    public static Recipe leatherBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Leather Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_LEATHER_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_LEATHER_BACKPACK, backpack);

        recipe.shape("FLF", "LCL", "FLF");

        recipe.setIngredient('C', Material.BARREL);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('F', Material.FEATHER);
        return recipe;
    }

    public static Recipe ironBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Iron Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_IRON_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_IRON_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', new RecipeChoice.MaterialChoice(Material.BARREL));
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public static Recipe goldBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Gold Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_GOLD_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_GOLD_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C',  new RecipeChoice.MaterialChoice(Material.BARREL));
        recipe.setIngredient('I', Material.GOLD_INGOT);

        return recipe;
    }

    public static Recipe lapisBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Lapis Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_LAPIS_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_LAPIS_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', new RecipeChoice.MaterialChoice(Material.BARREL));
        recipe.setIngredient('I', Material.LAPIS_LAZULI);

        return recipe;
    }

    public static Recipe amethystBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Amethyst Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_AMETHYST_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_AMETHYST_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', new RecipeChoice.MaterialChoice(Material.BARREL));
        recipe.setIngredient('I', Material.AMETHYST_SHARD);

        return recipe;
    }

    public static Recipe diamondBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Diamond Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_DIAMOND_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_DIAMOND_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', new RecipeChoice.MaterialChoice(Material.BARREL));
        recipe.setIngredient('I', Material.DIAMOND);

        return recipe;
    }

    public static Recipe netheriteBackpackRecipe() {
        ItemStack backpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Netherite Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_NETHERITE_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_NETHERITE_BACKPACK, backpack);

        recipe.shape(" I ", "ICI", " I ");

        recipe.setIngredient('C', new RecipeChoice.MaterialChoice(Material.BARREL));
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        return recipe;
    }

    public static FurnaceRecipe driedBackpackRecipe() {
        ItemStack driedBackpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();

        meta.setDisplayName("Unknown Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_DRIED_BACKPACK, PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);

        ItemStack wetBackpack = new ItemStack(Material.BARREL);
        ItemMeta wetBackpackMeta = wetBackpack.getItemMeta();

        wetBackpackMeta.setRarity(ItemRarity.EPIC);

        wetBackpackMeta.setDisplayName("Wet Backpack");
        wetBackpackMeta.setLore(Arrays.asList("Uhh, it looks really WET and unusable..", "Humm, what could I do with it?"));

        wetBackpackMeta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, -1);
        wetBackpackMeta.getPersistentDataContainer().set(NAMESPACE_WET_BACKPACK, PersistentDataType.INTEGER, 1);
        wetBackpack.setItemMeta(wetBackpackMeta);

        //200 game ticks
        return new FurnaceRecipe(NAMESPACE_DRIED_BACKPACK, driedBackpack, new RecipeChoice.ExactChoice(wetBackpack), 10, 200);
    }
}
