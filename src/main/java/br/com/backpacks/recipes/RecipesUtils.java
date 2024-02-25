package br.com.backpacks.recipes;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class RecipesUtils {

    public static BackPack getBackpackFromItem(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(!meta.getPersistentDataContainer().has(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER)) return null;

        return Main.backPackManager.getBackpackFromId(meta.getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
    }

    public static ItemStack getItemFromBackpack(BackPack backPack) {
        ItemStack itemStack = new ItemStack(Material.BARREL);
        ItemMeta meta = itemStack.getItemMeta();
        
        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
        meta.getPersistentDataContainer().set(backPack.getNamespace(), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static boolean isItemUpgrade(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER);
    }

    public static ItemStack getItemFromUpgrade(Upgrade upgrade) {
        ItemStack itemStack = new ItemStack(getMaterialFromUpgrade(upgrade));
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(getItemNameFromUpgrade(upgrade));
        meta.setLore(getLore(upgrade));
        meta.getPersistentDataContainer().set(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(getNamespaceFromUpgrade(upgrade), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new UpgradesRecipesNamespaces().getUPGRADEID(), PersistentDataType.INTEGER, upgrade.getId());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static String getItemNameFromUpgrade(Upgrade upgrade){
        switch (upgrade.getType()){
            case FURNACE: return "Furnace Upgrade";
            case SMOKER: return "Smoker Upgrade";
            case BLAST_FURNACE: return "Blast Furnace Upgrade";
            case CRAFTING: return "Crafting Table Upgrade";
            case JUKEBOX: return "Jukebox Upgrade";
            case VILLAGERSFOLLOW: return "Following Villagers Upgrade";
            case ENCAPSULATE: return "Encapsulate Upgrade";
            case COLLECTOR: return "Collector Upgrade";
            case AUTOFILL: return "Auto Fill Upgrade";
            case AUTOFEED: return "Auto Feed Upgrade";
            case UNBREAKABLE: return "Unbreakable Upgrade";
            case LIQUIDTANK: return "Liquid Tank Upgrade";
        }

        return "";
    }

    private static NamespacedKey getNamespaceFromUpgrade(Upgrade upgrade){
        switch (upgrade.getType()){
            case CRAFTING -> {
                return new UpgradesRecipesNamespaces().getCraftingGrid();
            }

            case UNBREAKABLE -> {
                return new UpgradesRecipesNamespaces().getUNBREAKING();
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
                return new UpgradesRecipesNamespaces().getFurnace();
            }

            case SMOKER -> {
                return new UpgradesRecipesNamespaces().getSMOKER();
            }

            case BLAST_FURNACE -> {
                return new UpgradesRecipesNamespaces().getBLASTFURNACE();
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

            case SMOKER -> {
                return Material.SMOKER;
            }

            case BLAST_FURNACE -> {
                return Material.BLAST_FURNACE;
            }
        }

        return Material.NETHERITE_INGOT;
    }

    public static Upgrade getUpgradeFromItem(ItemStack itemStack) {
        int id = itemStack.getItemMeta().getPersistentDataContainer().get(new UpgradesRecipesNamespaces().getUPGRADEID(), PersistentDataType.INTEGER);

        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getAutoFill(), PersistentDataType.INTEGER)) {
            if (UpgradeManager.getUpgradeFromId(id) != null) return UpgradeManager.getUpgradeFromId(id);
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.AUTOFILL, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getAutoFeed(), PersistentDataType.INTEGER)) {
            if (UpgradeManager.getUpgradeFromId(id) != null) return ((AutoFeedUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new AutoFeedUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getFurnace(), PersistentDataType.INTEGER)) {
            if (UpgradeManager.getUpgradeFromId(id) != null) return ((FurnaceUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new FurnaceUpgrade(UpgradeType.FURNACE, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getBLASTFURNACE(), PersistentDataType.INTEGER)) {
            if (UpgradeManager.getUpgradeFromId(id) != null) return ((FurnaceUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new FurnaceUpgrade(UpgradeType.BLAST_FURNACE, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getSMOKER(), PersistentDataType.INTEGER)) {
            if (UpgradeManager.getUpgradeFromId(id) != null) return ((FurnaceUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new FurnaceUpgrade(UpgradeType.SMOKER, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getCraftingGrid(), PersistentDataType.INTEGER)) {
            if(UpgradeManager.getUpgradeFromId(id) != null) return UpgradeManager.getUpgradeFromId(id);
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.CRAFTING, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getJukebox(), PersistentDataType.INTEGER)){
            if(UpgradeManager.getUpgradeFromId(id) != null) return ((JukeboxUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new JukeboxUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getVillagersFollow(), PersistentDataType.INTEGER)){
            if(UpgradeManager.getUpgradeFromId(id) != null) return ((VillagersFollowUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new VillagersFollowUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getLiquidTank(), PersistentDataType.INTEGER)) {
            if(UpgradeManager.getUpgradeFromId(id) != null) return (TanksUpgrade) UpgradeManager.getUpgradeFromId(id);
            UpgradeManager.getUpgrades().put(id, new TanksUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if (itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getENCAPSULATE(), PersistentDataType.INTEGER)) {
            if(UpgradeManager.getUpgradeFromId(id) != null) return UpgradeManager.getUpgradeFromId(id);
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.ENCAPSULATE, id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getCOLLECTOR(), PersistentDataType.INTEGER)){
            if(UpgradeManager.getUpgradeFromId(id) != null) return ((CollectorUpgrade) UpgradeManager.getUpgradeFromId(id));
            UpgradeManager.getUpgrades().put(id, new CollectorUpgrade(id));
            return UpgradeManager.getUpgrades().get(id);
        }
        if(itemStack.getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().getUNBREAKING(), PersistentDataType.INTEGER)){
            if(UpgradeManager.getUpgradeFromId(id) != null) return  UpgradeManager.getUpgradeFromId(id);
            UpgradeManager.getUpgrades().put(id, new Upgrade(UpgradeType.UNBREAKABLE, id));
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

            case BLAST_FURNACE -> {
                return Arrays.asList("§7Blast Furnace Upgrade", "§7§nAllows you to cook ores in the backpack.");
            }

            case SMOKER -> {
                return Arrays.asList("§7Smoker Upgrade", "§7§nAllows you to cook food in the backpack.");
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
        }

        return null;
    }
}