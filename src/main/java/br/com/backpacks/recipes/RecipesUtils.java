package br.com.backpacks.recipes;

import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class RecipesUtils {

    public static ItemStack getItemFromBackpack(BackPack backPack) {
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta meta = itemStack.getItemMeta();
        
        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
        meta.getPersistentDataContainer().set(backPack.getNamespaceOfBackpackType(), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getItemFromUpgrade(Upgrade upgrade) {
        ItemStack itemStack = new ItemStack(getMaterialFromUpgrade(upgrade));
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(getNameItemFromUpgrade(upgrade));
        meta.setLore(getLore(upgrade));
        meta.getPersistentDataContainer().set(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(getNamespaceFromUpgrade(upgrade), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String getNameItemFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case FURNACE: return "Furnace Grid";
            case CRAFTING: return "Crafting Grid";
            case JUKEBOX: return "Jukebox";
            case VILLAGERSFOLLOW: return "Following Villagers";
            case ENCAPSULATE: return "Encapsulate";
            case COLLECTOR: return "Collector";
            case AUTOFILL: return "Auto Fill";
            case AUTOFEED: return "Auto Food";
            case STACKUPGRADE2X: return "Stack Upgrade 2x";
            case STACKUPGRADE4X: return "Stack Upgrade 4x";
            case STACKUPGRADE8X: return "Stack Upgrade 8x";
            case STACKUPGRADE16X: return "Stack Upgrade 16x";
        }

        return "";
    }

    private static NamespacedKey getNamespaceFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case CRAFTING -> {
                return new UpgradesRecipesNamespaces().getCraftingGrid();
            }

            case JUKEBOX -> {
                return new UpgradesRecipesNamespaces().getJukebox();
            }

            case VILLAGERSFOLLOW -> {
                return new UpgradesRecipesNamespaces().getVillagersFollow();
            }

            case AUTOFILL -> {
                return new UpgradesRecipesNamespaces().getAutoFill();
            }

            case AUTOFEED -> {
                return new UpgradesRecipesNamespaces().getAutoFeed();
            }

            case FURNACE -> {
                return new UpgradesRecipesNamespaces().getFurnaceGrid();
            }

            case LIQUIDTANK -> {
                return new UpgradesRecipesNamespaces().getLiquidTank();
            }
            case ENCAPSULATE -> {
                return new UpgradesRecipesNamespaces().getENCAPSULATE();
            }

            case COLLECTOR -> {
                return new UpgradesRecipesNamespaces().getCOLLECTOR();
            }
        }

        return null;
    }

    private static Material getMaterialFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case CRAFTING -> {
                return Material.CRAFTING_TABLE;
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
        }

        return Material.NETHERITE_INGOT;
    }

    public static Upgrade getUpgradeFromItem(ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER))
            return null;

        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getAutoFill(), PersistentDataType.INTEGER))
            return Upgrade.AUTOFILL;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getAutoFeed(), PersistentDataType.INTEGER))
            return Upgrade.AUTOFEED;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getFurnaceGrid(), PersistentDataType.INTEGER))
            return Upgrade.FURNACE;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getCraftingGrid(), PersistentDataType.INTEGER))
            return Upgrade.CRAFTING;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getJukebox(), PersistentDataType.INTEGER))
            return Upgrade.JUKEBOX;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getVillagersFollow(), PersistentDataType.INTEGER))
            return Upgrade.VILLAGERSFOLLOW;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getLiquidTank(), PersistentDataType.INTEGER))
            return Upgrade.LIQUIDTANK;
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getENCAPSULATE(), PersistentDataType.INTEGER))
            return Upgrade.ENCAPSULATE;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getCOLLECTOR(), PersistentDataType.INTEGER))
            return Upgrade.COLLECTOR;

        return null;
    }

    private static List<String> getLore(Upgrade upgrade) {
        switch (upgrade) {
            case CRAFTING -> {
                return Arrays.asList("§7Crafting Grid", "§7§nAllows you to craft items in the backpack.");
            }

            case JUKEBOX -> {
                return Arrays.asList("§7Jukebox", "§7§nAllows you to play music discs in the backpack.");
            }

            case FURNACE -> {
                return Arrays.asList("§7Furnace Grid", "§7§nAllows you to cook items in the backpack.");
            }

            case VILLAGERSFOLLOW -> {
                return Arrays.asList("§7Emerald Block", "§7§nAllows you to attract villagers when the backpack is equipped and you are holding an emerald block.");
            }

            case ENCAPSULATE -> {
                return Arrays.asList("§7Encapsulate", "§7§nAllows you to store backpacks inside the backpack.");
            }

            case COLLECTOR -> {
                return Arrays.asList("§7Collector", "§7§nAllows you to collect items from the ground directly into your backpack."
                        , "§7§n§oThis upgrade only work if the backpack is being worn.");
            }
        }

        return null;
    }
}