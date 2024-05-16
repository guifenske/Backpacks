package br.com.backpacks.recipes;

import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RecipesUtils {

    public static BackPack getBackpackFromItem(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(!meta.getPersistentDataContainer().has(BackpackRecipes.isBackpack(), PersistentDataType.INTEGER)) return null;

        return BackpackManager.getBackpackFromId(meta.getPersistentDataContainer().get(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
    }

    public static ItemStack getItemFromBackpack(@NotNull BackPack backPack) {
        ItemStack itemStack = new ItemStack(Material.BARREL);
        ItemMeta meta = itemStack.getItemMeta();
        
        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(BackpackRecipes.isBackpack(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
        meta.getPersistentDataContainer().set(backPack.getNamespace(), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static boolean isItemUpgrade(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.isUpgrade(), PersistentDataType.INTEGER);
    }

    public static ItemStack getItemFromUpgrade(Upgrade upgrade) {
        ItemStack itemStack = new ItemStack(getMaterialFromUpgrade(upgrade));
        ItemMeta meta = itemStack.getItemMeta();
        UpgradeType upgradeType = upgrade.getType();

        meta.setDisplayName(upgradeType.toString().toUpperCase().charAt(0) + upgradeType.toString().replace("_", " ").substring(1).toLowerCase() + " Upgrade");
        meta.setLore(getLore(upgrade));
        meta.getPersistentDataContainer().set(UpgradesRecipes.isUpgrade(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(getNamespaceFromUpgrade(upgrade), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(UpgradesRecipes.getUPGRADE_ID(), PersistentDataType.INTEGER, upgrade.getId());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static NamespacedKey getNamespaceFromUpgrade(Upgrade upgrade){
        switch (upgrade.getType()){
            case CRAFTING -> {
                return UpgradesRecipes.getCraftingGrid();
            }

            case UNBREAKABLE -> {
                return UpgradesRecipes.getUnbreaking();
            }

            case JUKEBOX -> {
                return UpgradesRecipes.getJukebox();
            }

            case VILLAGERSFOLLOW -> {
                return UpgradesRecipes.getVillagersFollow();
            }

            case AUTOFILL -> {
                return UpgradesRecipes.getAutoFill();
            }

            case AUTOFEED -> {
                return UpgradesRecipes.getAutoFeed();
            }

            case FURNACE -> {
                return UpgradesRecipes.getFurnace();
            }

            case LIQUIDTANK -> {
                return UpgradesRecipes.getLiquidTank();
            }

            case ENCAPSULATE -> {
                return UpgradesRecipes.getEncapsulate();
            }

            case COLLECTOR -> {
                return UpgradesRecipes.getCollector();
            }

            case MAGNET -> {
                return UpgradesRecipes.getMagnet();
            }

            case FILTER -> {
                return UpgradesRecipes.getFilter();
            }

            case ADVANCED_FILTER -> {
                return UpgradesRecipes.getAdvancedFilter();
            }
        }

        return null;
    }

    private static Material getMaterialFromUpgrade(Upgrade upgrade){
        switch (upgrade.getType()){
            case CRAFTING -> {
                return Material.CRAFTING_TABLE;
            }

            case UNBREAKABLE -> {
                return Material.ENCHANTED_GOLDEN_APPLE;
            }

            case JUKEBOX -> {
                return Material.JUKEBOX;
            }

            case VILLAGERSFOLLOW -> {
                return Material.EMERALD_BLOCK;
            }

            case AUTOFILL -> {
                return Material.DISPENSER;
            }

            case AUTOFEED -> {
                return Material.COOKED_BEEF;
            }

            case FURNACE -> {
                return Material.FURNACE;
            }

            case LIQUIDTANK -> {
                return Material.BUCKET;
            }

            case ENCAPSULATE -> {
                return Material.GLASS_BOTTLE;
            }

            case COLLECTOR -> {
                return Material.HOPPER;
            }

            case MAGNET -> {
                return Material.ENDER_EYE;
            }

            case FILTER -> {
                return Material.CHEST;
            }

            case ADVANCED_FILTER -> {
                return Material.ENDER_CHEST;
            }
        }

        return Material.NETHERITE_INGOT;
    }

    public static Upgrade getUpgradeFromItem(ItemStack itemStack) {
        int id = itemStack.getItemMeta().getPersistentDataContainer().get(UpgradesRecipes.getUPGRADE_ID(), PersistentDataType.INTEGER);
        if (UpgradeManager.getUpgradeFromId(id) != null) return UpgradeManager.getUpgradeFromId(id);

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getAutoFill(), PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.AUTOFILL, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getAutoFeed(), PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new AutoFeedUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getFurnace(), PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new FurnaceUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getCraftingGrid(), PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.CRAFTING, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getJukebox(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new JukeboxUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getVillagersFollow(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new VillagersFollowUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getLiquidTank(), PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new TanksUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getEncapsulate(), PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.ENCAPSULATE, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getCollector(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new CollectorUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getUnbreaking(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.UNBREAKABLE, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getMagnet(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.MAGNET, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getFilter(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new FilterUpgrade(UpgradeType.FILTER, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.getAdvancedFilter(), PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new FilterUpgrade(UpgradeType.ADVANCED_FILTER, id));
            return UpgradeManager.getUpgrades().get(id);
        }

        return null;
    }

    private static List<String> getLore(Upgrade upgrade) {
        switch (upgrade.getType()) {
            case CRAFTING -> {
                return Arrays.asList("§7Crafting Table Upgrade", "§7§nAllows you to craft items in the backpack.");
            }

            case JUKEBOX -> {
                return Arrays.asList("§7Jukebox Upgrade", "§7§nAllows you to play music discs in the backpack.");
            }

            case FURNACE -> {
                return Arrays.asList("§7Furnace Upgrade", "§7§nAllows you to cook items in the backpack.");
            }

            case VILLAGERSFOLLOW -> {
                return Arrays.asList("§7Following Villagers Upgrade", "§7§nAllows you to attract villagers when the backpack is equipped",
                        "§7§n and you are holding an emerald block.");
            }

            case ENCAPSULATE -> {
                return Arrays.asList("§7Encapsulate Upgrade", "§7§nAllows you to store backpacks inside the backpack.");
            }

            case COLLECTOR -> {
                return Arrays.asList("§7Collector Upgrade", "§7§nAllows you to collect items from the ground directly into your backpack."
                        , "§7§n§oThis upgrade only work if the backpack is being worn.");
            }

            case AUTOFEED -> {
                return Arrays.asList("§7Auto Feed Upgrade", "§7§nAllows the backpack to automatically eat the food stored in it.");
            }

            case UNBREAKABLE -> {
                return Arrays.asList("§7Unbreakable Upgrade", "§7§nMake the backpack unbreakable.");
            }

            case LIQUIDTANK -> {
                return Arrays.asList("§7Liquid Tank Upgrade", "§7§nAllows you to store liquids in the backpack.");
            }

            case MAGNET -> {
                return Arrays.asList("§7Magnet Upgrade", "§7§nPull dropped items on the ground directly into your backpack.");
            }

            case FILTER -> {
                return Arrays.asList("§7Filter Upgrade", "§7§nOnly allows one specific item type to enter the backpack.");
            }

            case ADVANCED_FILTER -> {
                return Arrays.asList("§7Advanced Filter Upgrade", "§7§nOnly allows up to 9 specific items types to enter the backpack.");
            }
        }

        return null;
    }
}