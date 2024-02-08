package br.com.backpacks.backpackUtils.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.recipes.RecipesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryBuilder {
    private static final ConcurrentHashMap<Integer, Inventory> configMenu = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Inventory> upgradesMenu = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Inventory> editIOMenu = new ConcurrentHashMap<>();
    private MenuType type;
    private Integer backpackId;

    public static boolean containsEditIOMenu(BackPack backPack){
        return editIOMenu.containsKey(backPack.getId());
    }

    public static Inventory getConfigInv(BackPack backPack) {
        return configMenu.get(backPack.getId());
    }

    public static Inventory getUpgradesInv(BackPack backPack){
        return upgradesMenu.get(backPack.getId());
    }

    public static Inventory getIOInv(){
        Inventory inv = Bukkit.createInventory(null, 27, "I/O Menu");

        ItemStack resetDefault = new ItemCreator(Material.EMERALD_ORE, "Reset config").build();
        ItemStack input = new ItemCreator(Material.HOPPER, "Set new input inventory").build();
        ItemStack output = new ItemCreator(Material.DISPENSER, "Set new output inventory").build();

        inv.setItem(11, input);
        inv.setItem(13, resetDefault);
        inv.setItem(15, output);

        return inv;
    }

    public static Inventory getEditIOMenu(BackPack backPack){
        return editIOMenu.get(backPack.getId());
    }

    public InventoryBuilder(MenuType type, BackPack backPack){
        this.type = type;
        this.backpackId = backPack.getId();
    }

    public void build(){
        Inventory inv;
        switch (type){
            case CONFIG -> {
                inv = Bukkit.createInventory(null, 54, "Backpack Config");
                BackPack backPack = Main.backPackManager.getBackpackFromId(backpackId);

                ItemStack equipBackpack = new ItemCreator(Material.CHEST, "Equip Backpack").build();
                ItemStack unequipBackpack = new ItemCreator(Material.ENDER_CHEST, "Unequip Backpack").build();
                ItemStack close = new ItemCreator(Material.BARRIER, "Close").build();
                ItemStack loremIpsum = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();
                ItemStack rename = new ItemCreator(Material.NAME_TAG, "Rename Backpack").build();
                ItemStack lock = new ItemCreator(Material.WRITABLE_BOOK, "Lock Backpack", Arrays.asList("§7§nLock the access to this backpack", "§7§n from other players when in your back.")).build();
                ItemStack unlock = new ItemCreator(Material.WRITTEN_BOOK, "Unlock Backpack", Arrays.asList("§7§nUnlock the access to this backpack", "§7§n from other players when in your back.")).build();
                ItemStack editUpgrades = new ItemCreator(Material.ANVIL, "Edit Upgrades").build();
                ItemStack editIO = new ItemCreator(Material.HOPPER, "Sets the I/O inventory", Arrays.asList("§7Select upgrades to be the I/O inventories.")).build();

                for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 54; i++){
                    inv.setItem(i, loremIpsum);
                }

                if(!backPack.getUpgrades().isEmpty()) {
                    List<Upgrade> upgrades = backPack.getUpgrades();

                    int i = 0;
                    for(Upgrade upgrade : upgrades) {
                        inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                        i++;
                    }
                }

                if(!backPack.isBlock()) {
                    if (backPack.getOwner() != null) {
                        if(backPack.isLocked()) {
                            inv.setItem(51, unlock);
                        } else {
                            inv.setItem(51, lock);
                        }
                        inv.setItem(53, unequipBackpack);
                    } else{
                        inv.setItem(53, equipBackpack);
                    }
                }

                inv.setItem(36, editUpgrades);
                inv.setItem(52, rename);
                inv.setItem(45, close);
                inv.setItem(49, editIO);
                configMenu.put(backpackId, inv);
            }

            case UPGMENU -> {
                inv = Bukkit.createInventory(null, 9, "Upgrades Menu");

                BackPack backPack = Main.backPackManager.getBackpackFromId(backpackId);
                ItemStack item = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

                for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 9; i++){
                    inv.setItem(i, item);
                }

                int i = 0;
                List<Upgrade> upgrades = backPack.getUpgrades();
                if (!backPack.getUpgrades().isEmpty()) {
                    for (Upgrade upgrade : upgrades) {
                        inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                        i++;
                    }
                }

                upgradesMenu.put(backpackId, inv);
            }
            case EDIT_IO_MENU -> {
                inv = Bukkit.createInventory(null, 9, "Upgrades Menu");

                BackPack backPack = Main.backPackManager.getBackpackFromId(backpackId);
                ItemStack item = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

                for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 9; i++){
                    inv.setItem(i, item);
                }

                int i = 0;
                List<Upgrade> upgrades = backPack.getUpgrades();
                if (!backPack.getUpgrades().isEmpty()) {
                    for (Upgrade upgrade : upgrades) {
                        if(!upgrade.isAdvanced()) continue;
                        inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                        i++;
                    }
                }

                editIOMenu.put(backpackId, inv);
            }
        }
    }

    public static void deleteAllMenusFromBackpack(BackPack backPack){
        configMenu.remove(backPack.getId());
        upgradesMenu.remove(backPack.getId());
        editIOMenu.remove(backPack.getId());
    }

    public static void updateConfigInv(BackPack backPack){
        Inventory inv = configMenu.get(backPack.getId());
        ItemStack equipBackpack = new ItemCreator(Material.CHEST, "Equip Backpack").build();
        ItemStack unequipBackpack = new ItemCreator(Material.ENDER_CHEST, "Unequip Backpack").build();
        ItemStack close = new ItemCreator(Material.BARRIER, "Close").build();
        ItemStack loremIpsum = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();
        ItemStack rename = new ItemCreator(Material.NAME_TAG, "Rename Backpack").build();
        ItemStack lock = new ItemCreator(Material.WRITABLE_BOOK, "Lock Backpack", Arrays.asList("§7§nLock the access to this backpack", "§7§n from other players when in your back.")).build();
        ItemStack unlock = new ItemCreator(Material.WRITTEN_BOOK, "Unlock Backpack", Arrays.asList("§7§nUnlock the access to this backpack", "§7§n from other players when in your back.")).build();
        ItemStack editUpgrades = new ItemCreator(Material.ANVIL, "Edit Upgrades").build();
        ItemStack editIO = new ItemCreator(Material.HOPPER, "Sets the I/O inventory", Arrays.asList("§7Select upgrades to be the I/O inventories.")).build();

        for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 54; i++){
            inv.setItem(i, loremIpsum);
        }

        if(!backPack.getUpgrades().isEmpty()) {
            List<Upgrade> upgrades = backPack.getUpgrades();

            int i = 0;
            for(Upgrade upgrade : upgrades) {
                inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                i++;
            }
        }

        if(!backPack.isBlock()) {
            if (backPack.getOwner() != null) {
                if(backPack.isLocked()) {
                    inv.setItem(51, unlock);
                } else {
                    inv.setItem(51, lock);
                }
                inv.setItem(53, unequipBackpack);
            } else{
                inv.setItem(53, equipBackpack);
            }
        }

        inv.setItem(36, editUpgrades);
        inv.setItem(52, rename);
        inv.setItem(45, close);
        inv.setItem(49, editIO);
    }

    public static void updateUpgradesInv(BackPack backPack){
        Inventory inv = upgradesMenu.get(backPack.getId());
        ItemStack item = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 9; i++){
            inv.setItem(i, item);
        }

        int i = 0;
        List<Upgrade> upgrades = backPack.getUpgrades();
        if (!backPack.getUpgrades().isEmpty()) {
            for (Upgrade upgrade : upgrades) {
                inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                i++;
            }
        }
    }

    public static void updateEditIOInv(BackPack backPack){
        Inventory inv = editIOMenu.get(backPack.getId());
        ItemStack item = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 9; i++){
            inv.setItem(i, item);
        }

        int i = 0;
        List<Upgrade> upgrades = backPack.getUpgrades();
        if (!backPack.getUpgrades().isEmpty()) {
            for (Upgrade upgrade : upgrades) {
                if(!upgrade.isAdvanced()) continue;
                inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                i++;
            }
        }
    }

    public static int getFreeUpgradesSlots(BackpackType type){
        switch (type){
            case LEATHER: return 2;
            case IRON: return 3;
            case GOLD: return 4;
            case LAPIS: return 5;
            case AMETHYST: return 6;
            case DIAMOND: return 7;
            case NETHERITE: return 9;
        }

        return 0;
    }

    public enum MenuType {
        CONFIG,
        UPGMENU,
        EDIT_IO_MENU
    }
}