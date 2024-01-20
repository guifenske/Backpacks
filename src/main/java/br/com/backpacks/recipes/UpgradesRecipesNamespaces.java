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
    private final NamespacedKey VILLAGERSFOLLOW = new NamespacedKey(Main.getMain(), "villagerfollow");
    private final NamespacedKey ENCAPSULATE = new NamespacedKey(Main.getMain(), "encapsulate");
    private final NamespacedKey COLLECTOR = new NamespacedKey(Main.getMain(), "COLLECTOR");
    private final NamespacedKey AUTOFILL = new NamespacedKey(Main.getMain(), "autofill");
    private final NamespacedKey AUTOFEED = new NamespacedKey(Main.getMain(), "autofeed");
    private final NamespacedKey LIQUIDTANK = new NamespacedKey(Main.getMain(), "liquidtank");
    private final NamespacedKey UPGRADEID = new NamespacedKey(Main.getMain(), "upgradeid");
    private final NamespacedKey STACKUPGRADE2X = new NamespacedKey(Main.getMain(), "stackupgrade2x");
    private final NamespacedKey STACKUPGRADE4X = new NamespacedKey(Main.getMain(), "stackupgrade4x");
    private final NamespacedKey STACKUPGRADE8X = new NamespacedKey(Main.getMain(), "stackupgrade8x");
    private final NamespacedKey STACKUPGRADE16X = new NamespacedKey(Main.getMain(), "stackupgrade16x");
    private final NamespacedKey NAMESPACEISUPGRADE = new NamespacedKey(Main.getMain(), "isupgrade");

    public NamespacedKey isUpgrade() {
        return NAMESPACEISUPGRADE;
    }
    public NamespacedKey getUPGRADEID() {
        return UPGRADEID;
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

    public NamespacedKey getVillagersFollow() {
        return VILLAGERSFOLLOW;
    }

    public NamespacedKey getAutoFill() {
        return AUTOFILL;
    }

    public NamespacedKey getAutoFeed() {
        return AUTOFEED;
    }

    public NamespacedKey getLiquidTank() {
        return LIQUIDTANK;
    }
    public NamespacedKey getCOLLECTOR() {
        return COLLECTOR;
    }
    public NamespacedKey getENCAPSULATE() {
        return ENCAPSULATE;
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

    public Recipe getRecipeFollowingVillagers() {
        ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta = emeraldBlock.getItemMeta();

        meta.setDisplayName("Following Villagers");
        meta.setLore(Arrays.asList("§7Emerald Block", "§7§nAllows you to attract villagers when the backpack is equipped and you are holding an emerald block."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(VILLAGERSFOLLOW, PersistentDataType.INTEGER, 1);
        emeraldBlock.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(VILLAGERSFOLLOW, emeraldBlock);

        recipe.shape("GCS", "BTB", "EEE");

        recipe.setIngredient('B', Material.EMERALD_BLOCK);
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
        recipe.setIngredient('G', Material.GRINDSTONE);
        recipe.setIngredient('C', Material.CARTOGRAPHY_TABLE);
        recipe.setIngredient('S', Material.BREWING_STAND);


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

    public Recipe getRecipeAutoFeed(){
        ItemStack autoFood = new ItemStack(Material.COOKED_BEEF);
        ItemMeta meta = autoFood.getItemMeta();

        meta.setDisplayName("Auto Feed");
        meta.setLore(Arrays.asList("§7Auto Feed", "§7§nAllows the backpack to automatically eat the food stored in it."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
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

    public Recipe getEncapsulateRecipe(){
        ItemStack encapsulate = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = encapsulate.getItemMeta();

        meta.setDisplayName("Encapsulate");
        meta.setLore(Arrays.asList("§7Encapsulate", "§7§nAllows you to store backpacks inside the backpack."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
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

        meta.setDisplayName("Collector");
        meta.setLore(Arrays.asList("§7Collector", "§7§nAllows you to collect items from the ground directly into your backpack."
                                    , "§7§n§oThis upgrade only work if the backpack is being worn."));
        meta.getPersistentDataContainer().set(NAMESPACEISUPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(COLLECTOR, PersistentDataType.INTEGER, 1);
        collector.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(COLLECTOR, collector);

        recipe.shape("RIR", "IMI", "RIR");

        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('M', Material.CHEST_MINECART);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }
}