package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.RandomBackpackBuilder;
import br.com.backpacks.utils.inventory.InventoryBuilder;
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
        if(!event.getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DRIED_BACKPACK(), PersistentDataType.INTEGER)) return;

        ItemStack driedBackpack = new ItemStack(Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();
        meta.setDisplayName("Unknown Backpack");
        meta.getPersistentDataContainer().set(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, Main.backPackManager.getBackpackIds() + 1);
        RandomBackpackBuilder randomBackpackBuilder = new RandomBackpackBuilder("Unknown Backpack", meta.getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        BackPack backPack = randomBackpackBuilder.generateBackpack();
        meta.getPersistentDataContainer().set(backPack.getNamespace(), PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);
        Main.backPackManager.setBackpackIds(Main.backPackManager.getBackpackIds() + 1);
        new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack);
        new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack);
        new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack);
        event.setResult(driedBackpack);
    }
}
