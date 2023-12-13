package br.com.backpacks.backpackUtils.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class InventoryBuilder extends BackPack {

    public static Inventory mainConfigInv(Player player){
        Inventory inv = Bukkit.createInventory(player, 54, "Backpack Config");

        ItemStack equipBackpack = new ItemStack(Material.CHEST);
        ItemMeta equipBackpackMeta = equipBackpack.getItemMeta();
        equipBackpackMeta.setDisplayName("Equip Backpack");
        equipBackpackMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 2);
        equipBackpack.setItemMeta(equipBackpackMeta);

        ItemStack unequipBackpack = new ItemStack(Material.ENDER_CHEST);
        ItemMeta unequipBackpackMeta = unequipBackpack.getItemMeta();
        unequipBackpackMeta.setDisplayName("Un-equip Backpack");
        unequipBackpackMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 3);
        unequipBackpack.setItemMeta(unequipBackpackMeta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("Close");
        closeMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 4);
        close.setItemMeta(closeMeta);

        ItemStack loremIpsum = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta loremIpsumMeta = loremIpsum.getItemMeta();
        loremIpsumMeta.setDisplayName(" ");
        loremIpsumMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 6);
        loremIpsum.setItemMeta(loremIpsumMeta);

        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName("Rename Backpack");
        renameMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 5);
        rename.setItemMeta(renameMeta);

        ItemStack lock = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta lockMeta = lock.getItemMeta();
        lockMeta.setDisplayName("Lock Backpack");
        lockMeta.setLore(Arrays.asList("§7§nLock the access to this backpack", "§7§n from other players when in your back."));
        lockMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 7);
        lock.setItemMeta(lockMeta);

        ItemStack unlock = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta unlockMeta = unlock.getItemMeta();
        unlockMeta.setDisplayName("Unlock Backpack");
        unlockMeta.setLore(Arrays.asList("§7§nUnlock the access to this backpack", "§7§n from other players when in your back."));
        unlockMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 8);
        unlock.setItemMeta(unlockMeta);

        ItemStack editUpgrades = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta editUpgradesMeta = editUpgrades.getItemMeta();
        editUpgradesMeta.setDisplayName("Edit Upgrades");
        editUpgradesMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 9);
        editUpgrades.setItemMeta(editUpgradesMeta);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));

        for(int i = getFreeInitialSlots(backPack.getType()); i < 54; i++){
            inv.setItem(i, loremIpsum);
        }


        if(backPack.getUpgrades() != null) {
            if(!backPack.getUpgrades().isEmpty()) {
                List<Upgrade> upgrades = backPack.getUpgrades();

                for (int i = 0; i < upgrades.size(); i++) {
                    inv.setItem(i, Utils.getItemFromUpgrade(upgrades.get(i)));
                }
            }
        }

        if(!backPack.isBlock()) {
            if (player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) {
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
        return inv;
    }

    public static int getFreeInitialSlots(BackpackType type){
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