package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class RecipesUtils {

    public static BackPack getBackpackFromItem(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(!meta.getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK)) return null;

        return Main.backPackManager.getBackpackFromId(meta.getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER));
    }

    public static ItemStack getItemFromBackpack(BackPack backPack) {
        ItemStack itemStack = new ItemStack(Material.BARREL);
        ItemMeta meta = itemStack.getItemMeta();
        
        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(BackpackRecipes.IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER, backPack.getId());
        meta.getPersistentDataContainer().set(backPack.getType().getKey(), PersistentDataType.INTEGER, 1);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static boolean isItemUpgrade(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.IS_UPGRADE);
    }

    public static ItemStack getItemFromUpgrade(Upgrade upgrade) {
        ItemStack itemStack = new ItemStack(getMaterialFromUpgrade(upgrade));
        ItemMeta meta = itemStack.getItemMeta();
        UpgradeType upgradeType = upgrade.getType();

        meta.setDisplayName(upgradeType.toString().toUpperCase().charAt(0) + upgradeType.toString().toLowerCase().substring(1) + " Upgrade");
        meta.setLore(getLore(upgrade));

        meta.getPersistentDataContainer().set(UpgradesRecipes.IS_UPGRADE, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(upgradeType.getKey(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(UpgradesRecipes.UPGRADE_ID, PersistentDataType.INTEGER, upgrade.getId());

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static Material getMaterialFromUpgrade(Upgrade upgrade){
        switch (upgrade.getType()){
            case CRAFTING_GRID -> {
                return Material.CRAFTING_TABLE;
            }

            case UNBREAKABLE -> {
                return Material.ENCHANTED_GOLDEN_APPLE;
            }

            case JUKEBOX -> {
                return Material.JUKEBOX;
            }

            case VILLAGER_BAIT -> {
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

            case LIQUID_TANK -> {
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
        }

        return Material.NETHERITE_INGOT;
    }

    public static Upgrade getUpgradeFromItem(ItemStack itemStack) {
        int id = itemStack.getItemMeta().getPersistentDataContainer().get(UpgradesRecipes.UPGRADE_ID, PersistentDataType.INTEGER);
        if (UpgradeManager.getUpgradeFromId(id) != null) return UpgradeManager.getUpgradeFromId(id);

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.AUTOFILL, PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.AUTOFILL, id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.AUTOFEED, PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new AutoFeedUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.FURNACE, PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new FurnaceUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.CRAFTING_GRID, PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.CRAFTING_GRID, id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.JUKEBOX, PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new JukeboxUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.VILLAGER_BAIT, PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new VillagerBaitUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.LIQUID_TANK, PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new TanksUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if (itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.ENCAPSULATE, PersistentDataType.INTEGER)) {
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.ENCAPSULATE, id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.COLLECTOR, PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new CollectorUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.UNBREAKABLE, PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.UNBREAKABLE, id));
            return UpgradeManager.getUpgrades().get(id);
        }

        if(itemStack.getItemMeta().getPersistentDataContainer().has(UpgradesRecipes.MAGNET, PersistentDataType.INTEGER)){
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.MAGNET, id));
            return UpgradeManager.getUpgrades().get(id);
        }

        return null;
    }

    private static List<String> getLore(Upgrade upgrade) {
        switch (upgrade.getType()) {
            case CRAFTING_GRID -> {
                return Arrays.asList("§7Crafting Table Upgrade", "§7§nAllows you to craft items in the backpack.");
            }

            case JUKEBOX -> {
                return Arrays.asList("§7Jukebox Upgrade", "§7§nAllows you to play music discs in the backpack.");
            }

            case FURNACE -> {
                return Arrays.asList("§7Furnace Upgrade", "§7§nAllows you to cook items in the backpack.");
            }

            case VILLAGER_BAIT -> {
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

            case LIQUID_TANK -> {
                return Arrays.asList("§7Liquid Tank Upgrade", "§7§nAllows you to store liquids in the backpack.");
            }
            case MAGNET -> {
                return Arrays.asList("§7Magnet Upgrade", "§7§nPull dropped items on the ground directly into your backpack.");
            }
        }

        return null;
    }
}