package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class UpgradesRecipes {
    public static final NamespacedKey CRAFTING_GRID = new NamespacedKey(Main.getMain(), "crafting_grid");
    public static final NamespacedKey FURNACE = new NamespacedKey(Main.getMain(), "furnace");
    public static final NamespacedKey UNBREAKABLE = new NamespacedKey(Main.getMain(), "unbreakable");
    public static final NamespacedKey JUKEBOX = new NamespacedKey(Main.getMain(), "jukebox");
    public static final NamespacedKey VILLAGER_BAIT = new NamespacedKey(Main.getMain(), "villager_bait");
    public static final NamespacedKey ENCAPSULATE = new NamespacedKey(Main.getMain(), "encapsulate");
    public static final NamespacedKey COLLECTOR = new NamespacedKey(Main.getMain(), "collector");
    public static final NamespacedKey AUTOFILL = new NamespacedKey(Main.getMain(), "autofill");
    public static final NamespacedKey AUTOFEED = new NamespacedKey(Main.getMain(), "autofeed");
    public static final NamespacedKey LIQUID_TANK = new NamespacedKey(Main.getMain(), "liquid_tank");
    public static final NamespacedKey UPGRADE_ID = new NamespacedKey(Main.getMain(), "upgrade_id");
    public static final NamespacedKey MAGNET = new NamespacedKey(Main.getMain(), "magnet");
    public static final NamespacedKey IS_UPGRADE = new NamespacedKey(Main.getMain(), "is_upgrade");

    public static Recipe getCraftingTableRecipe() {
        ItemStack craftingGrid = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = craftingGrid.getItemMeta();

        meta.setDisplayName("Crafting Table Upgrade");
        meta.setLore(Arrays.asList("§7Crafting Table Upgrade", "§7§nAllows you to craft items in the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(CRAFTING_GRID, PersistentDataType.INTEGER, 1);
        craftingGrid.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(CRAFTING_GRID, craftingGrid);

        recipe.shape("LIL", "ICI", "LIL");

        recipe.setIngredient('C', Material.CRAFTING_TABLE);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('L', Material.LEATHER);

        return recipe;
    }

    public static Recipe getJukeboxRecipe() {
        ItemStack jukebox = new ItemStack(Material.JUKEBOX);
        ItemMeta meta = jukebox.getItemMeta();

        meta.setDisplayName("Jukebox Upgrade");
        meta.setLore(Arrays.asList("§7Jukebox Upgrade", "§7§nAllows you to play music discs in the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
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

    public static Recipe getFollowingVillagersRecipe() {
        ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta = emeraldBlock.getItemMeta();

        meta.setDisplayName("Following Villagers Upgrade");
        meta.setLore(Arrays.asList("§Following Villagers Upgrade", "§7§nAllows you to attract villagers when the backpack is equipped",
                "§7§n and you are holding an emerald block."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(VILLAGER_BAIT, PersistentDataType.INTEGER, 1);
        emeraldBlock.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(VILLAGER_BAIT, emeraldBlock);

        recipe.shape("GCS", "BTB", "EEE");

        recipe.setIngredient('B', Material.EMERALD_BLOCK);
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
        recipe.setIngredient('G', Material.GRINDSTONE);
        recipe.setIngredient('C', Material.CARTOGRAPHY_TABLE);
        recipe.setIngredient('S', Material.BREWING_STAND);


        return recipe;
    }

    public static Recipe getAutoFillRecipe(){
        ItemStack autoFill = new ItemStack(Material.DISPENSER);
        ItemMeta meta = autoFill.getItemMeta();

        meta.setDisplayName("Auto Fill Upgrade");
        meta.setLore(Arrays.asList("§7Auto Fill Upgrade", "§7§nAutomatically fills an item of your choice in the desired slot."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(AUTOFILL, PersistentDataType.INTEGER, 1);
        autoFill.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(AUTOFILL, autoFill);

        recipe.shape("RDR", "DCD", "RDR");

        recipe.setIngredient('D', Material.DISPENSER);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('C', Material.CHEST);

        return recipe;
    }

    public static Recipe getFurnaceRecipe(){
        ItemStack furnaceGrid = new ItemStack(Material.FURNACE);
        ItemMeta meta = furnaceGrid.getItemMeta();

        meta.setDisplayName("Furnace Upgrade");
        meta.setLore(Arrays.asList("§7Furnace Upgrade", "§7§nAllows you to cook items in the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(FURNACE, PersistentDataType.INTEGER, 1);
        furnaceGrid.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(FURNACE, furnaceGrid);

        recipe.shape("III", "RFR", "III");

        recipe.setIngredient('F', Material.FURNACE);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('R', Material.REDSTONE);

        return recipe;
    }

    public static Recipe getAutoFeedRecipe(){
        ItemStack autoFood = new ItemStack(Material.COOKED_BEEF);
        ItemMeta meta = autoFood.getItemMeta();

        meta.setDisplayName("Auto Feed Upgrade");
        meta.setLore(Arrays.asList("§7Auto Feed Upgrade", "§7§nAllows the backpack to automatically eat the food stored in it."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(AUTOFEED, PersistentDataType.INTEGER, 1);
        autoFood.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(AUTOFEED, autoFood);

        recipe.shape("IRI", "BGC", "IPI");

        recipe.setIngredient('R', Material.RABBIT_STEW);
        recipe.setIngredient('G', Material.GLISTERING_MELON_SLICE);
        recipe.setIngredient('B', Material.COOKED_BEEF);
        recipe.setIngredient('C', Material.GOLDEN_CARROT);
        recipe.setIngredient('P', Material.POISONOUS_POTATO);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public static Recipe getLiquidTankRecipe() {
        ItemStack liquidTank = new ItemStack(Material.BUCKET);
        ItemMeta meta = liquidTank.getItemMeta();

        meta.setDisplayName("Liquid Tank Upgrade");
        meta.setLore(Arrays.asList("§7Liquid Tank Upgrade", "§7§nAllows you to store liquids in the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(LIQUID_TANK, PersistentDataType.INTEGER, 1);
        liquidTank.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(LIQUID_TANK, liquidTank);

        recipe.shape("BGB", "BGB", "BGB");

        recipe.setIngredient('B', Material.BUCKET);
        recipe.setIngredient('G', Material.GLASS_PANE);

        return recipe;
    }

    public static Recipe getEncapsulateRecipe(){
        ItemStack encapsulate = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = encapsulate.getItemMeta();

        meta.setDisplayName("Encapsulate Upgrade");
        meta.setLore(Arrays.asList("§7Encapsulate Upgrade", "§7§nAllows you to store backpacks inside the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(ENCAPSULATE, PersistentDataType.INTEGER, 1);
        encapsulate.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(ENCAPSULATE, encapsulate);

        recipe.shape("GNG", "GSG", "GPG");

        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('P', Material.PISTON);
        recipe.setIngredient('G', Material.GLASS);

        return recipe;
    }

    public static Recipe getCollectorRecipe(){
        ItemStack collector = new ItemStack(Material.HOPPER);
        ItemMeta meta = collector.getItemMeta();

        meta.setDisplayName("Collector Upgrade");
        meta.setLore(Arrays.asList("§7Collector Upgrade", "§7§nAllows you to collect items from the ground directly into your backpack."
                                    , "§7§n§oThis upgrade only work if the backpack is being worn."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(COLLECTOR, PersistentDataType.INTEGER, 1);
        collector.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(COLLECTOR, collector);

        recipe.shape("RIR", "IMI", "RIR");

        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('M', Material.CHEST_MINECART);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public static Recipe getUnbreakableUpgradeRecipe(){
        ItemStack unbreak = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemMeta meta = unbreak.getItemMeta();

        meta.setDisplayName("Unbreakable Upgrade");
        meta.setLore(Arrays.asList("§7Unbreakable Upgrade", "§7§nMake the backpack unbreakable."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(UNBREAKABLE, PersistentDataType.INTEGER, 1);
        unbreak.setItemMeta(meta);

        ItemStack unbreakingThree = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta2 = unbreakingThree.getItemMeta();
        meta2.addEnchant(Enchantment.UNBREAKING, 3, false);
        unbreakingThree.setItemMeta(meta2);

        ShapedRecipe recipe = new ShapedRecipe(UNBREAKABLE, unbreak);

        recipe.shape("NSN", "UUU", "NSN");

        recipe.setIngredient('S', Material.NETHERITE_INGOT);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('U', new RecipeChoice.ExactChoice(unbreakingThree));

        return recipe;
    }

    public static Recipe getMagnetRecipe() {
        ItemStack magnet = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = magnet.getItemMeta();

        meta.setDisplayName("Magnet Upgrade");
        meta.setLore(Arrays.asList("§7Magnet Upgrade", "§7§nPull dropped items on the ground directly into your backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(MAGNET, PersistentDataType.INTEGER, 1);
        magnet.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(MAGNET, magnet);
        recipe.shape("IRI", "IEI", "IRI");

        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('E', Material.ENDER_PEARL);
        recipe.setIngredient('R', Material.REDSTONE);

        return recipe;
    }
}