package br.com.backpacks.events.entity;

import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import br.com.backpacks.utils.backpacks.RandomBackpackBuilder;
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
        if(!event.getResult().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.getNAMESPACE_DRIED_BACKPACK(), PersistentDataType.INTEGER)) return;

        ItemStack driedBackpack = new ItemStack(Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();
        meta.setDisplayName("Unknown Backpack");
        meta.getPersistentDataContainer().set(BackpackRecipes.isBackpack(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, BackpackManager.lastBackpackID.get() + 1);
        RandomBackpackBuilder randomBackpackBuilder = new RandomBackpackBuilder("Unknown Backpack", meta.getPersistentDataContainer().get(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        BackPack backPack = randomBackpackBuilder.generateBackpack();
        meta.getPersistentDataContainer().set(backPack.getNamespace(), PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);
        BackpackManager.lastBackpackID.getAndIncrement();
        new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
        new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
        new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
        event.setResult(driedBackpack);
    }
}
