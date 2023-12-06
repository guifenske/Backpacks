package br.com.backpacks.backpackUtils.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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

        ItemStack loremIpsum = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta loremIpsumMeta = loremIpsum.getItemMeta();
        loremIpsumMeta.setDisplayName("Lorem Ipsum");
        loremIpsumMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 6);
        loremIpsum.setItemMeta(loremIpsumMeta);

        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName("Rename Backpack");
        renameMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 5);
        rename.setItemMeta(renameMeta);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isInBackpackConfig.get(player.getUniqueId()));

        if(!backPack.isBlock()) {
            if (player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) {
                inv.setItem(53, unequipBackpack);
            } else inv.setItem(53, equipBackpack);
        }

        for(int i = getFreeInitialSlots(backPack.getType()); i < 54; i++){
            inv.setItem(i, loremIpsum);
        }

        inv.setItem(52, rename);

        inv.setItem(45, close);

        return inv;
    }

    private static int getFreeInitialSlots(BackpackType type){
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