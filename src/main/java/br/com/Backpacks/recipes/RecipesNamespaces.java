package br.com.Backpacks.recipes;

import br.com.Backpacks.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Set;

public class RecipesNamespaces {
    public Set<Recipe> get_backpacks_recipes = new HashSet<>();

    public NamespacedKey getIS_BACKPACK() {
        return IS_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_BACKPACK_ID() {
        return NAMESPACE_BACKPACK_ID;
    }

    private final NamespacedKey NAMESPACE_BACKPACK_ID = new NamespacedKey(Main.back, "backpackid");

    private final NamespacedKey NAMESPACE_HAS_BACKPACK = new NamespacedKey(Main.back, "hasbackpack");

    public NamespacedKey getNAMESPACE_HAS_BACKPACK(){
        return NAMESPACE_HAS_BACKPACK;
    }

    public Boolean has_backpack(LivingEntity entity){
        return entity.getPersistentDataContainer().has(NAMESPACE_HAS_BACKPACK, PersistentDataType.INTEGER);
    }

    public Boolean has_backpack(Player entity){
        return entity.getPersistentDataContainer().has(NAMESPACE_HAS_BACKPACK, PersistentDataType.INTEGER);
    }

    private final NamespacedKey IS_BACKPACK = new NamespacedKey(Main.back, "isbackpack");

    public NamespacedKey getNAMESPACE_LEATHER_BACKPACK() {
        return NAMESPACE_LEATHER_BACKPACK;
    }

    private final NamespacedKey NAMESPACE_LEATHER_BACKPACK = new NamespacedKey(Main.back, "leatherbackpack");

    public NamespacedKey getNAMESPACE_IRON_BACKPACK() {
        return NAMESPACE_IRON_BACKPACK;
    }

    private final NamespacedKey NAMESPACE_IRON_BACKPACK = new NamespacedKey(Main.back, "ironbackpack");
    private final NamespacedKey NAMESPACE_GOLD_BACKPACK = new NamespacedKey(Main.back, "goldbackpack");
    private final NamespacedKey NAMESPACE_LAPIS_BACKPACK = new NamespacedKey(Main.back, "lapisbackpack");
    private final NamespacedKey NAMESPACE_AMETHYST_BACKPACK = new NamespacedKey(Main.back, "amethystbackpack");

    public NamespacedKey getNAMESPACE_GOLD_BACKPACK() {
        return NAMESPACE_GOLD_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_LAPIS_BACKPACK() {
        return NAMESPACE_LAPIS_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_DIAMOND_BACKPACK() {
        return NAMESPACE_DIAMOND_BACKPACK;
    }

    private final NamespacedKey NAMESPACE_DIAMOND_BACKPACK = new NamespacedKey(Main.back, "diamondbackpack");

    public NamespacedKey getNAMESPACE_AMETHYST_BACKPACK() {
        return NAMESPACE_AMETHYST_BACKPACK;
    }

    public NamespacedKey getNAMESPACE_NETHERITE_BACKPACK() {
        return NAMESPACE_NETHERITE_BACKPACK;
    }

    private final NamespacedKey NAMESPACE_NETHERITE_BACKPACK = new NamespacedKey(Main.back, "netheritebackpack");

    public Recipe leather_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Leather Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_LEATHER_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_LEATHER_BACKPACK, backpack);

        recipe.shape("FLF", "LCL", "FLF");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('F', Material.FEATHER);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }

    public Recipe iron_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Iron Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_IRON_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_IRON_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.IRON_INGOT);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }

    public Recipe gold_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Gold Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_GOLD_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_GOLD_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.GOLD_INGOT);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }

    public Recipe lapis_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Lapis Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_LAPIS_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_LAPIS_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.LAPIS_LAZULI);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }

    public Recipe amethyst_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Amethyst Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_AMETHYST_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_AMETHYST_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.AMETHYST_SHARD);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }

    public Recipe diamond_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Diamond Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_DIAMOND_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_DIAMOND_BACKPACK, backpack);

        recipe.shape("III", "ICI", "III");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.DIAMOND);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }

    public Recipe netherite_backpack_recipe(){
        ItemStack backpack = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();

        meta.setDisplayName("Netherite Backpack");
        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(NAMESPACE_NETHERITE_BACKPACK, PersistentDataType.INTEGER, 1);

        backpack.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NAMESPACE_NETHERITE_BACKPACK, backpack);

        recipe.shape("AAA", "ICI", "AAA");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.AIR);

        get_backpacks_recipes.add(recipe);

        return recipe;
    }
}
