package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class UpgradesRecipes {
    private final NamespacedKey CRAFTING = new NamespacedKey(Main.getMain(), "crafting_upgrade");
    private final NamespacedKey FURNACE = new NamespacedKey(Main.getMain(), "furnace_upgrade");
    private final NamespacedKey UNBREAKING = new NamespacedKey(Main.getMain(), "unbreaking_upgrade");
    private final NamespacedKey JUKEBOX = new NamespacedKey(Main.getMain(), "jukebox_upgrade");
    private final NamespacedKey VILLAGERS_FOLLOW = new NamespacedKey(Main.getMain(), "villagers_follow_upgrade");
    private final NamespacedKey ENCAPSULATE = new NamespacedKey(Main.getMain(), "encapsulate_upgrade");
    private final NamespacedKey COLLECTOR = new NamespacedKey(Main.getMain(), "collector_upgrade");
    private final NamespacedKey AUTO_FILL = new NamespacedKey(Main.getMain(), "auto_fill_upgrade");
    private final NamespacedKey AUTO_FEED = new NamespacedKey(Main.getMain(), "auto_feed_upgrade");
    private final NamespacedKey LIQUID_TANK = new NamespacedKey(Main.getMain(), "liquid_tank_upgrade");
    private final NamespacedKey MAGNET = new NamespacedKey(Main.getMain(), "magnet_upgrade");
    private final NamespacedKey FILTER = new NamespacedKey(Main.getMain(), "filter_upgrade");
    private final NamespacedKey ADVANCED_FILTER = new NamespacedKey(Main.getMain(), "advanced_filter_upgrade");
    private final NamespacedKey UPGRADE_ID = new NamespacedKey(Main.getMain(), "upgrade_id");
    private final NamespacedKey IS_UPGRADE = new NamespacedKey(Main.getMain(), "is_upgrade");

    public NamespacedKey isUpgrade() {
        return IS_UPGRADE;
    }
    public NamespacedKey getUPGRADE_ID() {
        return UPGRADE_ID;
    }
    public NamespacedKey getCraftingGrid() {
        return CRAFTING;
    }
    public NamespacedKey getFurnace() {
        return FURNACE;
    }
    public NamespacedKey getUnbreaking() {
        return UNBREAKING;
    }
    public NamespacedKey getJukebox() {
        return JUKEBOX;
    }
    public NamespacedKey getVillagersFollow() {
        return VILLAGERS_FOLLOW;
    }
    public NamespacedKey getAutoFill() {
        return AUTO_FILL;
    }
    public NamespacedKey getAutoFeed() {
        return AUTO_FEED;
    }
    public NamespacedKey getLiquidTank() {
        return LIQUID_TANK;
    }
    public NamespacedKey getCollector() {
        return COLLECTOR;
    }
    public NamespacedKey getEncapsulate() {
        return ENCAPSULATE;
    }
    public NamespacedKey getMagnet() {
        return MAGNET;
    }
    public NamespacedKey getFilter(){
        return FILTER;
    }
    public NamespacedKey getAdvancedFilter(){
        return ADVANCED_FILTER;
    }

    public Recipe getCraftingRecipe() {
        ItemStack craftingGrid = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = craftingGrid.getItemMeta();

        meta.setDisplayName("Crafting Table Upgrade");
        meta.setLore(Arrays.asList("§7Crafting Table Upgrade", "§7§nAllows you to craft items in the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(CRAFTING, PersistentDataType.INTEGER, 1);
        craftingGrid.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(CRAFTING, craftingGrid);

        recipe.shape("LIL", "ICI", "LIL");

        recipe.setIngredient('C', Material.CRAFTING_TABLE);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('L', Material.LEATHER);

        return recipe;
    }

    public Recipe getJukeboxRecipe() {
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

    public Recipe getFollowingVillagersRecipe() {
        ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta = emeraldBlock.getItemMeta();

        meta.setDisplayName("Following Villagers Upgrade");
        meta.setLore(Arrays.asList("§Following Villagers Upgrade", "§7§nAllows you to attract villagers when the backpack is equipped",
                "§7§n and you are holding an emerald block."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(VILLAGERS_FOLLOW, PersistentDataType.INTEGER, 1);
        emeraldBlock.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(VILLAGERS_FOLLOW, emeraldBlock);

        recipe.shape("GCS", "BTB", "EEE");

        recipe.setIngredient('B', Material.EMERALD_BLOCK);
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
        recipe.setIngredient('G', Material.GRINDSTONE);
        recipe.setIngredient('C', Material.CARTOGRAPHY_TABLE);
        recipe.setIngredient('S', Material.BREWING_STAND);


        return recipe;
    }

    public Recipe getAutoFillRecipe(){
        ItemStack autoFill = new ItemStack(Material.DISPENSER);
        ItemMeta meta = autoFill.getItemMeta();

        meta.setDisplayName("Auto Fill Upgrade");
        meta.setLore(Arrays.asList("§7Auto Fill Upgrade", "§7§nAutomatically fills an item of your choice in the desired slot."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(AUTO_FILL, PersistentDataType.INTEGER, 1);
        autoFill.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(AUTO_FILL, autoFill);

        recipe.shape("RDR", "DCD", "RDR");

        recipe.setIngredient('D', Material.DISPENSER);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('C', Material.CHEST);

        return recipe;
    }

    public Recipe getFurnaceRecipe(){
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

    public Recipe getAutoFeedRecipe(){
        ItemStack autoFood = new ItemStack(Material.COOKED_BEEF);
        ItemMeta meta = autoFood.getItemMeta();

        meta.setDisplayName("Auto Feed Upgrade");
        meta.setLore(Arrays.asList("§7Auto Feed Upgrade", "§7§nAllows the backpack to automatically eat the food stored in it."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(AUTO_FEED, PersistentDataType.INTEGER, 1);
        autoFood.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(AUTO_FEED, autoFood);

        recipe.shape("IRI", "BGC", "IPI");

        recipe.setIngredient('R', Material.RABBIT_STEW);
        recipe.setIngredient('G', Material.GLISTERING_MELON_SLICE);
        recipe.setIngredient('B', Material.COOKED_BEEF);
        recipe.setIngredient('C', Material.GOLDEN_CARROT);
        recipe.setIngredient('P', Material.POISONOUS_POTATO);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public Recipe getLiquidTankRecipe() {
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

    public Recipe getEncapsulateRecipe(){
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

    public Recipe getCollectorRecipe(){
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
        recipe.setIngredient('M', Material.HOPPER_MINECART);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public Recipe getUnbreakableUpgradeRecipe(){
        ItemStack unbreak = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemMeta meta = unbreak.getItemMeta();

        meta.setDisplayName("Unbreakable Upgrade");
        meta.setLore(Arrays.asList("§7Unbreakable Upgrade", "§7§nMake the backpack unbreakable."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(UNBREAKING, PersistentDataType.INTEGER, 1);
        unbreak.setItemMeta(meta);

        ItemStack unbreakingThree = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta2 = unbreakingThree.getItemMeta();
        meta2.addEnchant(Enchantment.DURABILITY, 3, false);
        unbreakingThree.setItemMeta(meta2);

        ShapedRecipe recipe = new ShapedRecipe(UNBREAKING, unbreak);

        recipe.shape("NSN", "UUU", "NSN");

        recipe.setIngredient('S', Material.NETHERITE_INGOT);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('U', unbreakingThree);

        return recipe;
    }

    public Recipe getMagnetRecipe() {
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

    public Recipe getFilterRecipe(){
        ItemStack filter = new ItemStack(Material.CHEST);
        ItemMeta meta = filter.getItemMeta();

        meta.setDisplayName("Filter Upgrade");
        meta.setLore(Arrays.asList("§7Filter Upgrade", "§7§nOnly allows one specific item type to enter the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(FILTER, PersistentDataType.INTEGER, 1);
        filter.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(FILTER, filter);
        recipe.shape("IHI", "IFI", "IRI");

        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('F', Material.ITEM_FRAME);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('H', Material.HOPPER);

        return recipe;
    }

    public Recipe getAdvancedFilterUpgrade(){
        ItemStack filter = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = filter.getItemMeta();

        meta.setDisplayName("Advanced Filter Upgrade");
        meta.setLore(Arrays.asList("§7Advanced Filter Upgrade", "§7§nOnly allows up to 9 specific items types to enter the backpack."));
        meta.getPersistentDataContainer().set(IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(ADVANCED_FILTER, PersistentDataType.INTEGER, 1);
        filter.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(ADVANCED_FILTER, filter);
        recipe.shape("FFF", "FCF", "FFF");

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('F', Material.ITEM_FRAME);

        return recipe;
    }
}