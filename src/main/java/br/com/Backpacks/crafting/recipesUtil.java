package br.com.Backpacks.crafting;

import br.com.Backpacks.Main;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import static java.util.Arrays.asList;

public class recipesUtil {
    public static ItemStack leatherItem;
    public static ItemStack goldItem;
    public static ItemStack ironItem;
    public static ItemStack lapisItem;
    public static ItemStack amethystItem;
    public static ItemStack diamondItem;
    public static ItemStack upgraderItem;
    public static ItemStack leatherUpgraderItem;
    public static ItemStack goldUpgraderItem;
    public static ItemStack ironUpgraderItem;
    public static ItemStack lapisUpgraderItem;
    public static ItemStack amethystUpgraderItem;
    public static ItemStack leatherchestitem;
    public static ItemStack ironChestItem;
    public static ItemStack chainChestItem;
    public static ItemStack goldChestItem;
    public static ItemStack diamondChestItem;
    public static ItemStack netheriteChestItem;
    public static ItemStack uncrafterItem;
    public static ItemStack netheriteItem;
    public static ItemStack ironStackUpgraderItem;
    public static ItemStack goldStackUpgraderItem;
    public static ItemStack diamondStackUpgraderItem;
    public static ItemStack netheriteStackUpgraderItem;
    public static NamespacedKey leatherkey;
    public static NamespacedKey goldkey;
    public static NamespacedKey ironkey;
    public static NamespacedKey lapiskey;
    public static NamespacedKey amethystkey;
    public static NamespacedKey diamondkey;
    public static NamespacedKey netheriteKey;
    public static NamespacedKey upgraderkey;
    public static NamespacedKey leatherUpgraderKey;
    public static NamespacedKey goldUpgraderKey;
    public static NamespacedKey ironUpgraderKey;
    public static NamespacedKey lapisUpgraderKey;
    public static NamespacedKey amethystUpgraderKey;
    public static NamespacedKey leatherchestkey;
    public static NamespacedKey ironChestKey;
    public static NamespacedKey chainChestKey;
    public static NamespacedKey goldChestKey;
    public static NamespacedKey diamondChestkey;
    public static NamespacedKey netheriteChestkey;
    public static NamespacedKey uncrafterkey;
    public static NamespacedKey ironStackUpgraderKey;
    public static NamespacedKey goldStackUpgraderKey;
    public static NamespacedKey diamondStackUpgraderKey;
    public static NamespacedKey netheriteStackUpgraderKey;

    public static Recipe LeatherBackpack() {
        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Leather Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_leatherbackpack", true);

        leatherItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "leather_backpack");

        leatherkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("FLF", "LCL", "FLF");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('F', Material.FEATHER);

        return recipe;
    }

    public static Recipe Upgrader() {
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Backpack Upgrader");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("is_upgrader", true);
        list.setBoolean("base", true);

        NamespacedKey key = new NamespacedKey(Main.back, "backpack_upgrader");
        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());
        recipe.shape("LIL", "ICI", "LIL");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        upgraderkey = key;
        upgraderItem = nbtItem.getItem();

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('I', Material.IRON_INGOT);


        return recipe;
    }

    public static Recipe LeatherUpgrader() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Leather Backpack Upgrader");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("is_upgrader", true);
        list.setBoolean("leather", true);

        NamespacedKey key = new NamespacedKey(Main.back, "leatherbackpack_upgrader");
        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());
        recipe.shape("III", "ICI", "ILI");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        leatherUpgraderKey = key;
        leatherUpgraderItem = nbtItem.getItem();

        recipe.setIngredient('C', upgraderItem);
        recipe.setIngredient('L', Material.CHEST);
        recipe.setIngredient('I', Material.GOLD_INGOT);


        return recipe;
    }

    public static Recipe GoldUpgrader() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Gold Backpack Upgrader");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("is_upgrader", true);
        list.setBoolean("gold", true);

        NamespacedKey key = new NamespacedKey(Main.back, "goldbackpack_upgrader");
        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());
        recipe.shape("III", "ICI", "ILI");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        goldUpgraderKey = key;
        goldUpgraderItem = nbtItem.getItem();

        recipe.setIngredient('C', leatherUpgraderItem);
        recipe.setIngredient('L', Material.CHEST);
        recipe.setIngredient('I', Material.IRON_INGOT);


        return recipe;
    }

    public static Recipe IronUpgrader() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Iron Backpack Upgrader");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("is_upgrader", true);
        list.setBoolean("iron", true);

        NamespacedKey key = new NamespacedKey(Main.back, "ironbackpack_upgrader");
        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());
        recipe.shape("III", "ICI", "ILI");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        ironUpgraderKey = key;
        ironUpgraderItem = nbtItem.getItem();

        recipe.setIngredient('C', goldUpgraderItem);
        recipe.setIngredient('L', Material.CHEST);
        recipe.setIngredient('I', Material.LAPIS_LAZULI);


        return recipe;
    }

    public static Recipe LapisUpgrader() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Lapis Backpack Upgrader");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("is_upgrader", true);
        list.setBoolean("lapis", true);

        NamespacedKey key = new NamespacedKey(Main.back, "lapisbackpack_upgrader");
        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());
        recipe.shape("III", "ICI", "ILI");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        lapisUpgraderKey = key;
        lapisUpgraderItem = nbtItem.getItem();

        recipe.setIngredient('C', ironUpgraderItem);
        recipe.setIngredient('L', Material.CHEST);
        recipe.setIngredient('I', Material.AMETHYST_SHARD);


        return recipe;
    }

    public static Recipe AmethystUpgrader() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Amethyst Backpack Upgrader");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("is_upgrader", true);
        list.setBoolean("amethyst", true);

        NamespacedKey key = new NamespacedKey(Main.back, "amethystbackpack_upgrader");
        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());
        recipe.shape("III", "ICI", "ILI");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        amethystUpgraderKey = key;
        amethystUpgraderItem = nbtItem.getItem();

        recipe.setIngredient('C', lapisUpgraderItem);
        recipe.setIngredient('L', Material.CHEST);
        recipe.setIngredient('I', Material.DIAMOND);


        return recipe;
    }

    public static Recipe GoldBackpack() {

        LeatherUpgrader();

        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Gold Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_goldbackpack", true);

        goldItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "gold_backpack");

        goldkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("CU");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('U', leatherUpgraderItem);

        return recipe;
    }

    public static Recipe IronBackpack() {
        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Iron Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_ironbackpack", true);

        ironItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "iron_backpack");

        ironkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("CU");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('U', goldUpgraderItem);


        return recipe;
    }

    public static Recipe LapisBackpack() {
        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Lapis Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_lapisbackpack", true);

        lapisItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "lapis_backpack");

        lapiskey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("CU");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('U', ironUpgraderItem);


        return recipe;
    }

    public static Recipe AmethystBackpack() {
        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Amethyst Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_amethystbackpack", true);

        amethystItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "amethyst_backpack");

        amethystkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("CU");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('U', lapisUpgraderItem);


        return recipe;
    }

    public static Recipe DiamondBackpack() {
        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Diamond Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_diamondbackpack", true);

        diamondItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "diamond_backpack");

        diamondkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("CU");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('U', amethystUpgraderItem);


        return recipe;
    }

    public static Recipe NetheriteBackpack() {
        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Netherite Backpack");

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("backpack", true);
        list.setBoolean("is_netheritebackpack", true);


        netheriteItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "netherite_backpack");

        netheriteKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("ICN");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('C', Material.CHEST);
        recipe.setIngredient('I', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);

        return recipe;
    }

    public static Recipe leatherChestBack() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Leather BackpackChestplate");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("chestbackpack", true);

        leatherchestitem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "leatherbackpackchestplate");

        leatherchestkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "ECE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.LEATHER_CHESTPLATE);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);


        return recipe;
    }

    public static Recipe chainChestBack() {
        ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chainmail BackpackChestplate");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("chestbackpack", true);

        chainChestItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "chainbackpackchestplate");

        chainChestKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "ECE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.CHAINMAIL_CHESTPLATE);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);


        return recipe;
    }

    public static Recipe ironChestBack() {
        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Iron BackpackChestplate");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("chestbackpack", true);

        ironChestItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "ironbackpackchestplate");

        ironChestKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "ECE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.IRON_CHESTPLATE);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);


        return recipe;
    }

    public static Recipe goldChestBack() {
        ItemStack item = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Gold BackpackChestplate");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("chestbackpack", true);

        goldChestItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "goldbackpackchestplate");

        goldChestKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "ECE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.GOLDEN_CHESTPLATE);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);


        return recipe;
    }

    public static Recipe diamondChestBack() {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Diamond BackpackChestplate");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("chestbackpack", true);

        diamondChestItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "diamondbackpackchestplate");

        diamondChestkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "ECE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.DIAMOND_CHESTPLATE);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);


        return recipe;
    }

    public static Recipe netheriteChestBack() {
        ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Netherite BackpackChestplate");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("chestbackpack", true);

        netheriteChestItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "netheritebackpackchestplate");

        netheriteChestkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "ECE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.NETHERITE_CHESTPLATE);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.AIR);


        return recipe;
    }



    public static Recipe Uncrafter() {
        ItemStack item = new ItemStack(Material.DROPPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Uncrafter of Backpack");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        NBTCompound list = nbtItem.getCompound("backpack_tags");
        list.setBoolean("uncrafter", true);

        uncrafterItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "backpack_uncrafter");

        uncrafterkey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("AAA", "LBL", "EEE");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('B', Material.DROPPER);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('A', Material.STRING);


        return recipe;
    }

    public static Recipe IronStackUpgrader() {
        ItemStack item = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Iron Stack Upgrader");
        meta.setLore(asList(
                "Multiplies the max stack size of items by 2x"
        ));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        nbtItem.getCompound("backpack_tags").setBoolean("upgrade", true);
        nbtItem.getCompound("backpack_tags").setBoolean("ironstackupgrader", true);
        nbtItem.getCompound("backpack_tags").setBoolean("stackupgrader", true);

        ironStackUpgraderItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "backpack_iron_stackupgrader");

        ironStackUpgraderKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("III", "ICI", "III");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('C', Material.CHEST);


        return recipe;
    }

    public static Recipe GoldStackUpgrader() {
        ItemStack item = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Gold Stack Upgrader");
        meta.setLore(asList(
                "Multiplies the max stack size of items by 4x"
        ));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        nbtItem.getCompound("backpack_tags").setBoolean("upgrade", true);
        nbtItem.getCompound("backpack_tags").setBoolean("goldstackupgrader", true);
        nbtItem.getCompound("backpack_tags").setBoolean("stackupgrader", true);

        goldStackUpgraderItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "backpack_gold_stackupgrader");

        goldStackUpgraderKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("III", "ICI", "III");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('I', Material.GOLD_BLOCK);
        recipe.setIngredient('C', Material.CHEST);


        return recipe;
    }

    public static Recipe DiamondStackUpgrader() {
        ItemStack item = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Diamond Stack Upgrader");
        meta.setLore(asList(
                "Multiplies the max stack size of items by 8x"
        ));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        nbtItem.getCompound("backpack_tags").setBoolean("upgrade", true);
        nbtItem.getCompound("backpack_tags").setBoolean("diamondstackupgrader", true);
        nbtItem.getCompound("backpack_tags").setBoolean("stackupgrader", true);

        diamondStackUpgraderItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "backpack_diamond_stackupgrader");

        diamondStackUpgraderKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("III", "ICI", "III");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('I', Material.DIAMOND_BLOCK);
        recipe.setIngredient('C', Material.CHEST);


        return recipe;
    }

    public static Recipe NetheriteStackUpgrader() {
        ItemStack item = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Netherite Stack Upgrader");
        meta.setLore(asList(
                "Multiplies the max stack size of items by 8x"
        ));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound("backpack_tags");
        nbtItem.getCompound("backpack_tags").setBoolean("upgrade", true);
        nbtItem.getCompound("backpack_tags").setBoolean("netheritestackupgrader", true);
        nbtItem.getCompound("backpack_tags").setBoolean("stackupgrader", true);

        diamondStackUpgraderItem = nbtItem.getItem();

        NamespacedKey key = new NamespacedKey(Main.back, "backpack_netherite_stackupgrader");

        diamondStackUpgraderKey = key;

        ShapedRecipe recipe = new ShapedRecipe(key, nbtItem.getItem());

        recipe.shape("III", "ICI", "III");

        recipe.setCategory(CraftingBookCategory.EQUIPMENT);

        recipe.setIngredient('I', Material.NETHERITE_BLOCK);
        recipe.setIngredient('C', Material.CHEST);


        return recipe;
    }

    public static ItemStack checkBackRecipes(ItemStack item) {

        if(item.equals(leatherItem)) return item;

        if (item.equals(goldItem)) return item;

        if (item.equals(ironItem)) return item;

        if (item.equals(lapisItem)) return item;

        if (item.equals(amethystItem)) return item;

        if (item.equals(diamondItem)) return item;

        if(item.equals(netheriteItem)) return item;

        return null;
    }

    public static ItemStack checkChestBack(ItemStack item) {
        if (item.equals(leatherchestitem)) return item;

        if (item.equals(chainChestItem)) return item;

        if (item.equals(ironChestItem)) return item;

        if (item.equals(goldChestItem)) return item;

        if (item.equals(diamondChestItem)) return item;

        if (item.equals(netheriteChestItem)) return item;

        return null;
    }

    public static ItemStack checkArmor(ItemStack item) {
        if(item.getType().equals(Material.LEATHER_CHESTPLATE)) return item;

        if(item.getType().equals(Material.CHAINMAIL_CHESTPLATE)) return item;

        if(item.getType().equals(Material.IRON_CHESTPLATE)) return item;

        if(item.getType().equals(Material.GOLDEN_CHESTPLATE)) return item;

        if(item.getType().equals(Material.DIAMOND_CHESTPLATE)) return item;

        if(item.getType().equals(Material.NETHERITE_CHESTPLATE)) return item;

        return null;
    }

    public static ItemStack checkUpgrader(ItemStack item) {
        if (item.equals(leatherUpgraderItem)) return item;

        if (item.equals(goldUpgraderItem)) return item;

        if (item.equals(ironUpgraderItem)) return item;

        if (item.equals(lapisUpgraderItem)) return item;

        if (item.equals(amethystUpgraderItem)) return item;

        if (item.equals(upgraderItem)) return item;

        return null;
    }
}
