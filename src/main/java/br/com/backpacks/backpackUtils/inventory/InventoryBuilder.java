package br.com.backpacks.backpackUtils.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.RecipesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Set;

public class InventoryBuilder {

    public static Inventory mainConfigInv(Player player){
        Inventory inv = Bukkit.createInventory(player, 54, "Backpack Config");
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));

        ItemStack equipBackpack = new ItemCreator(Material.CHEST, "Equip Backpack").get();
        ItemStack unequipBackpack = new ItemCreator(Material.ENDER_CHEST, "Unequip Backpack").get();
        ItemStack close = new ItemCreator(Material.BARRIER, "Close").get();
        ItemStack loremIpsum = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").get();
        ItemStack rename = new ItemCreator(Material.NAME_TAG, "Rename Backpack").get();
        ItemStack lock = new ItemCreator(Material.WRITABLE_BOOK, "Lock Backpack", Arrays.asList("§7§nLock the access to this backpack", "§7§n from other players when in your back.")).get();
        ItemStack unlock = new ItemCreator(Material.WRITTEN_BOOK, "Unlock Backpack", Arrays.asList("§7§nUnlock the access to this backpack", "§7§n from other players when in your back.")).get();
        ItemStack editUpgrades = new ItemCreator(Material.ANVIL, "Edit Upgrades").get();
        ItemStack inputOutput = new ItemCreator(Material.HOPPER, "Input/Output").get();

        for(int i = getFreeUpgradesSlots(backPack.getType()); i < 54; i++){
            inv.setItem(i, loremIpsum);
        }

        if(backPack.getUpgrades() != null) {
            if(!backPack.getUpgrades().isEmpty()) {
                Set<Upgrade> upgrades = backPack.getUpgrades();

                int i = 0;
                for(Upgrade upgrade : upgrades) {
                    inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                    i++;
                }
            }
        }

        if(!backPack.isBlock()) {
            if (player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK()) && player.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER) == backPack.getId()) {
                if(backPack.isLocked()) {
                    inv.setItem(51, unlock);
                } else {
                    inv.setItem(51, lock);
                }
                inv.setItem(53, unequipBackpack);
            } else if(!player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) {
                inv.setItem(53, equipBackpack);
            }
        }

        inv.setItem(49, inputOutput);
        inv.setItem(36, editUpgrades);
        inv.setItem(52, rename);
        inv.setItem(45, close);
        return inv;
    }

    public static Inventory inputOutputMenu(Player player){
        Inventory inventory = Bukkit.createInventory(player, 27, "Input/Output");
        ItemStack input = new ItemCreator(Material.HOPPER, "Input").get();
        ItemStack output = new ItemCreator(Material.DROPPER, "Output").get();
        ItemStack close = new ItemCreator(Material.BARRIER, "Close").get();
        ItemStack loremIpsum = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").get();

        for(int i = 0; i < 27; i++){
            inventory.setItem(i, loremIpsum);
        }

        inventory.setItem(11, input);
        inventory.setItem(15, output);
        inventory.setItem(22, close);

        return inventory;
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
}