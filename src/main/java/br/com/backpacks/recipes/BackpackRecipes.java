package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class BackpackRecipes {
    private final NamespacedKey IS_BACKPACK = new NamespacedKey(Main.getMain(), "isbackpack");
    private final NamespacedKey BACKPACK_ID = new NamespacedKey(Main.getMain(), "backpackid");
    private final NamespacedKey HAS_BACKPACK = new NamespacedKey(Main.getMain(), "hasbackpack");
    private final NamespacedKey IS_CONFIG_ITEM = new NamespacedKey(Main.getMain(), "isconfigitem");
    private final NamespacedKey NAMESPACE_LEATHER_BACKPACK = new NamespacedKey(Main.getMain(), "leatherbackpack");
    private final NamespacedKey NAMESPACE_IRON_BACKPACK = new NamespacedKey(Main.getMain(), "ironbackpack");
    private final NamespacedKey NAMESPACE_GOLD_BACKPACK = new NamespacedKey(Main.getMain(), "goldbackpack");
    private final NamespacedKey NAMESPACE_LAPIS_BACKPACK = new NamespacedKey(Main.getMain(), "lapisbackpack");
    private final NamespacedKey NAMESPACE_AMETHYST_BACKPACK = new NamespacedKey(Main.getMain(), "amethystbackpack");
    private final NamespacedKey NAMESPACE_DRIED_BACKPACK = new NamespacedKey(Main.getMain(), "driedbackpack");
    private final NamespacedKey NAMESPACE_WET_BACKPACK = new NamespacedKey(Main.getMain(), "wetbackpack");
    private final NamespacedKey NAMESPACE_DIAMOND_BACKPACK = new NamespacedKey(Main.getMain(), "diamondbackpack");
    private final NamespacedKey NAMESPACE_NETHERITE_BACKPACK = new NamespacedKey(Main.getMain(), "netheritebackpack");

    public NamespacedKey isBackpack() {
        return IS_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_DRIED_BACKPACK() {
        return NAMESPACE_DRIED_BACKPACK;
    }

    public NamespacedKey getIS_CONFIG_ITEM() {
        return IS_CONFIG_ITEM;
    }

    public NamespacedKey getNAMESPACE_IRON_BACKPACK() {
        return NAMESPACE_IRON_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_LEATHER_BACKPACK() {
        return NAMESPACE_LEATHER_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_WET_BACKPACK() {
        return NAMESPACE_WET_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_BACKPACK_ID() {
        return BACKPACK_ID;
    }

    public NamespacedKey getHAS_BACKPACK() {
        return HAS_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_AMETHYST_BACKPACK() {
        return NAMESPACE_AMETHYST_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_NETHERITE_BACKPACK() {
        return NAMESPACE_NETHERITE_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_GOLD_BACKPACK() {
        return NAMESPACE_GOLD_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_LAPIS_BACKPACK() {
        return NAMESPACE_LAPIS_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_DIAMOND_BACKPACK() {
        return NAMESPACE_DIAMOND_BACKPACK;
    }

    public Recipe leatherBackpackRecipe() {
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

    public Recipe ironBackpackRecipe() {
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

    public Recipe goldBackpackRecipe() {
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

    public Recipe lapisBackpackRecipe() {
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

    public Recipe amethystBackpackRecipe() {
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

    public Recipe diamondBackpackRecipe() {
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

    public Recipe netheriteBackpackRecipe() {
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

    public FurnaceRecipe driedBackpackRecipe() {
        ItemStack driedBackpack = new ItemStack(org.bukkit.Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();

        meta.setDisplayName("Unknown Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_DRIED_BACKPACK, PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);

        ItemStack wetBackpack = new ItemStack(Material.BARREL);
        ItemMeta wetBackpackMeta = wetBackpack.getItemMeta();
        wetBackpackMeta.setDisplayName("Wet Backpack");
        wetBackpackMeta.setLore(Arrays.asList("Uhh, it looks really WET and unusable..", "Humm, what i could do with it?"));
        wetBackpackMeta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, -1);
        wetBackpackMeta.getPersistentDataContainer().set(NAMESPACE_WET_BACKPACK, PersistentDataType.INTEGER, 1);
        wetBackpack.setItemMeta(wetBackpackMeta);

        //200 game ticks
        return new FurnaceRecipe(NAMESPACE_DRIED_BACKPACK, driedBackpack, new RecipeChoice.ExactChoice(wetBackpack), 10, 200);
    }
}
