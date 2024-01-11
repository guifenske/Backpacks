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
            case EMERALDBLOCK: return "Emerald Block";
            case AUTOFILL: return "Auto Fill";
            case AUTOFOOD: return "Auto Food";
            case TRASH: return "Trash Can";
            case STACKUPGRADE2X: return "Stack Upgrade 2x";
            case STACKUPGRADE4X: return "Stack Upgrade 4x";
            case STACKUPGRADE8X: return "Stack Upgrade 8x";
            case STACKUPGRADE16X: return "Stack Upgrade 16x";
        }

        return "";
    }

    private static NamespacedKey getNamespaceFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case TRASH -> {
                return new UpgradesRecipesNamespaces().getTRASHCAN();
            }

            case CRAFTING -> {
                return new UpgradesRecipesNamespaces().getCraftingGrid();
            }

            case JUKEBOX -> {
                return new UpgradesRecipesNamespaces().getJukebox();
            }

            case EMERALDBLOCK -> {
                return new UpgradesRecipesNamespaces().getEmeraldBlock();
            }

            case AUTOFILL -> {
                return new UpgradesRecipesNamespaces().getAutoFill();
            }

            case AUTOFOOD -> {
                return new UpgradesRecipesNamespaces().getAutoFood();
            }

            case FURNACE -> {
                return new UpgradesRecipesNamespaces().getFurnaceGrid();
            }

            case LIQUIDTANK -> {
                return new UpgradesRecipesNamespaces().getLiquidTank();
            }
        }

        return null;
    }

    private static Material getMaterialFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case TRASH -> {
                return Material.CAULDRON;
            }

            case CRAFTING -> {
                return Material.CRAFTING_TABLE;
            }

            case JUKEBOX -> {
                return Material.JUKEBOX;
            }

            case EMERALDBLOCK -> {
                return Material.EMERALD_BLOCK;
            }

            case AUTOFILL -> {
                return Material.DISPENSER;
            }

            case AUTOFOOD -> {
                return Material.MELON;
            }

            case FURNACE -> {
                return Material.FURNACE;
            }

            case LIQUIDTANK -> {
                return Material.BUCKET;
            }
        }

        return Material.NETHERITE_INGOT;
    }

    public static Upgrade getUpgradeFromItem(ItemStack itemStack){
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)) return null;

        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getTRASHCAN(), PersistentDataType.INTEGER)) return Upgrade.TRASH;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getAutoFill(), PersistentDataType.INTEGER)) return Upgrade.AUTOFILL;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getAutoFood(), PersistentDataType.INTEGER)) return Upgrade.AUTOFOOD;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getFurnaceGrid(), PersistentDataType.INTEGER)) return Upgrade.FURNACE;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getCraftingGrid(), PersistentDataType.INTEGER)) return Upgrade.CRAFTING;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getJukebox(), PersistentDataType.INTEGER)) return Upgrade.JUKEBOX;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getEmeraldBlock(), PersistentDataType.INTEGER)) return Upgrade.EMERALDBLOCK;
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getLiquidTank(), PersistentDataType.INTEGER)) return Upgrade.LIQUIDTANK;

        return null;
    }

    public static boolean upgradeCanStack(Upgrade upgrade) {
        switch (upgrade) {
            case STACKUPGRADE2X, STACKUPGRADE4X, STACKUPGRADE8X, STACKUPGRADE16X -> {
                return true;
            }
        }

        return false;
    }

    private static List<String> getLore(Upgrade upgrade) {
        switch (upgrade) {
            case TRASH -> {
                return Arrays.asList("§7Trash Can", "§7§nRemoves one slot from the backpack, to be used ", "§7§nas an trash slot.");
            }

            case CRAFTING -> {
                return Arrays.asList("§7Crafting Grid", "§7§nAllows you to craft items in the backpack.");
            }

            case JUKEBOX -> {
                return Arrays.asList("§7Jukebox", "§7§nAllows you to play music discs in the backpack.");
            }

            case FURNACE -> {
                return Arrays.asList("§7Furnace Grid", "§7§nAllows you to cook items in the backpack.");
            }
        }

        return null;
    }
}