package br.com.backpacks.recipes;

import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Utils {

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
        meta.getPersistentDataContainer().set(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(getNamespaceFromUpgrade(upgrade), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static String getNameItemFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case FURNACE: return "Furnace";
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
        }

        return null;
    }

    private static Material getMaterialFromUpgrade(Upgrade upgrade){
        switch (upgrade){
            case TRASH -> {
                return Material.CAULDRON;
            }
        }

        return Material.NETHERITE_INGOT;
    }

    public static Upgrade getUpgradeFromItem(ItemStack itemStack){
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getTRASHCAN(), PersistentDataType.INTEGER)){
            return Upgrade.TRASH;
        }

        return null;
    }
}