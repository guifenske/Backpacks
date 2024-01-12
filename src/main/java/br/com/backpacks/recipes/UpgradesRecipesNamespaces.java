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
    private final NamespacedKey CRAFTINGGRID = new NamespacedKey(Main.getMain(), "craftinggrid");
    private final NamespacedKey FURNACEGRID = new NamespacedKey(Main.getMain(), "furnacegrid");
    private final NamespacedKey JUKEBOX = new NamespacedKey(Main.getMain(), "jukebox");
    private final NamespacedKey EMERALDBLOCK = new NamespacedKey(Main.getMain(), "emeraldblock");
    private final NamespacedKey AUTOFILL = new NamespacedKey(Main.getMain(), "autofill");
    private final NamespacedKey AUTOFOOD = new NamespacedKey(Main.getMain(), "autofood");
    private final NamespacedKey LIQUIDTANK = new NamespacedKey(Main.getMain(), "liquidtank");
    private final NamespacedKey STACKUPGRADE2X = new NamespacedKey(Main.getMain(), "stackupgrade2x");
    private final NamespacedKey STACKUPGRADE4X = new NamespacedKey(Main.getMain(), "stackupgrade4x");
    private final NamespacedKey STACKUPGRADE8X = new NamespacedKey(Main.getMain(), "stackupgrade8x");
    private final NamespacedKey STACKUPGRADE16X = new NamespacedKey(Main.getMain(), "stackupgrade16x");

    private final NamespacedKey NAMESPACEISUPGRADE = new NamespacedKey(Main.getMain(), "isupgrade");

    public NamespacedKey isUpgrade() {
        return NAMESPACEISUPGRADE;
    }

    public NamespacedKey getCraftingGrid() {
        return CRAFTINGGRID;
    }

    public NamespacedKey getFurnaceGrid() {
        return FURNACEGRID;
    }

    public NamespacedKey getJukebox() {
        return JUKEBOX;
    }

    public NamespacedKey getEmeraldBlock() {
        return EMERALDBLOCK;
    }

    public NamespacedKey getAutoFill() {
        return AUTOFILL;
    }

    public NamespacedKey getAutoFood() {
        return AUTOFOOD;
    }

    public NamespacedKey getLiquidTank() {
        return LIQUIDTANK;
    }

    public Recipe getRecipeCraftingGrid() {
        ItemStack craftingGrid = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = craftingGrid.getItemMeta();

        meta.setDisplayName("Crafting Grid");
        meta.setLore(Arrays.asList("§7Crafting Grid", "§7§nAllows you to craft items in the backpack."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(CRAFTINGGRID, PersistentDataType.INTEGER, 1);
        craftingGrid.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(CRAFTINGGRID, craftingGrid);

        recipe.shape("LIL", "ICI", "LIL");

        recipe.setIngredient('C', Material.CRAFTING_TABLE);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('L', Material.LEATHER);

        return recipe;
    }

    public Recipe getRecipeJukebox() {
        ItemStack jukebox = new ItemStack(Material.JUKEBOX);
        ItemMeta meta = jukebox.getItemMeta();

        meta.setDisplayName("Jukebox");
        meta.setLore(Arrays.asList("§7Jukebox", "§7§nAllows you to play music discs in the backpack."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(JUKEBOX, PersistentDataType.INTEGER, 1);
        jukebox.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(JUKEBOX, jukebox);

        recipe.shape("RIR", "DJD", "RCR");

        recipe.setIngredient('J', Material.JUKEBOX);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('C', Material.CHEST);

        return recipe;
    }

    public Recipe getRecipeEmeraldBlock() {
        ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta = emeraldBlock.getItemMeta();

        meta.setDisplayName("Emerald Block");
        meta.setLore(Arrays.asList("§7Emerald Block", "§7§nAllows you to attract villagers when the backpack is equipped."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(EMERALDBLOCK, PersistentDataType.INTEGER, 1);
        emeraldBlock.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(EMERALDBLOCK, emeraldBlock);

        recipe.shape("EEE", "ETE", "ESE");

        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
        recipe.setIngredient('S', Material.STRING);

        return recipe;
    }

    public Recipe getRecipeAutoFill(){
        ItemStack autoFill = new ItemStack(Material.DISPENSER);
        ItemMeta meta = autoFill.getItemMeta();

        meta.setDisplayName("Auto Fill");
        meta.setLore(Arrays.asList("§7Auto Fill", "§7§nAutomatically fills an item of your choice in the desired slot."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(AUTOFILL, PersistentDataType.INTEGER, 1);
        autoFill.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(AUTOFILL, autoFill);

        recipe.shape("RDR", "DCD", "RDR");

        recipe.setIngredient('D', Material.DISPENSER);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('C', Material.CHEST);

        return recipe;
    }

    public Recipe getRecipeFurnaceGrid(){
        ItemStack furnaceGrid = new ItemStack(Material.FURNACE);
        ItemMeta meta = furnaceGrid.getItemMeta();

        meta.setDisplayName("Furnace Grid");
        meta.setLore(Arrays.asList("§7Furnace Grid", "§7§nAllows you to cook items in the backpack."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(FURNACEGRID, PersistentDataType.INTEGER, 1);
        furnaceGrid.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(FURNACEGRID, furnaceGrid);

        recipe.shape("III", "RFR", "III");

        recipe.setIngredient('F', Material.FURNACE);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('R', Material.REDSTONE);

        return recipe;
    }

    public Recipe getRecipeAutoFood(){
        ItemStack autoFood = new ItemStack(Material.MELON);
        ItemMeta meta = autoFood.getItemMeta();

        meta.setDisplayName("Auto Food");
        meta.setLore(Arrays.asList("§7Auto Food", "§7§nAllows you to automatically eat the food stored in the backpack."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(AUTOFOOD, PersistentDataType.INTEGER, 1);
        autoFood.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(AUTOFOOD, autoFood);

        recipe.shape("IRI", "BGC", "IPI");

        recipe.setIngredient('R', Material.RABBIT_STEW);
        recipe.setIngredient('G', Material.GLISTERING_MELON_SLICE);
        recipe.setIngredient('B', Material.BEEF);
        recipe.setIngredient('C', Material.GOLDEN_CARROT);
        recipe.setIngredient('P', Material.BAKED_POTATO);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public Recipe getRecipeLiquidTank() {
        ItemStack liquidTank = new ItemStack(Material.BUCKET);
        ItemMeta meta = liquidTank.getItemMeta();

        meta.setDisplayName("Liquid Tank");
        meta.setLore(Arrays.asList("§7Liquid Tank", "§7§nAllows you to store liquids in the backpack."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(LIQUIDTANK, PersistentDataType.INTEGER, 1);
        liquidTank.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(LIQUIDTANK, liquidTank);

        recipe.shape("BGB", "BGB", "BGB");

        recipe.setIngredient('B', Material.BUCKET);
        recipe.setIngredient('G', Material.GLASS_PANE);

        return recipe;
    }
}