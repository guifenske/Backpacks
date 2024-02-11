package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.RandomBackpack;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FinishedSmelting implements Listener {

    @EventHandler
    private void onSmelt(BlockCookEvent event){
        if(!event.getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DRIED_BACKPACK())) return;

        ItemStack driedBackpack = new ItemStack(Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();
        meta.setDisplayName("Unknown Backpack");
        meta.getPersistentDataContainer().set(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, Main.backPackManager.getBackpackIds() + 1);
        RandomBackpack randomBackpack = new RandomBackpack("Unknown Backpack", meta.getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        BackPack backPack = randomBackpack.generateBackpack();
        meta.getPersistentDataContainer().set(backPack.getNamespace(), PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);
        Main.backPackManager.setBackpackIds(Main.backPackManager.getBackpackIds() + 1);
        new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack);
        new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack);
        new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack);
        event.setResult(driedBackpack);
    }
}
