package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.RandomBackpackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FurnaceEvents implements Listener {

    @EventHandler
    private void onSmelt(BlockCookEvent event){
        if(!event.getResult().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.NAMESPACE_DRIED_BACKPACK)) return;

        ItemStack driedBackpack = new ItemStack(Material.BARREL);
        ItemMeta meta = driedBackpack.getItemMeta();
        meta.setDisplayName("Unknown Backpack");

        meta.getPersistentDataContainer().set(BackpackRecipes.IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER, Main.backpackManager.getLastBackpackID() + 1);

        RandomBackpackBuilder randomBackpackBuilder = new RandomBackpackBuilder("Unknown Backpack", meta.getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER));
        Backpack backpack = randomBackpackBuilder.generateBackpack();

        meta.getPersistentDataContainer().set(backpack.getType().getKey(), PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);
        Main.backpackManager.setLastBackpackID(Main.backpackManager.getLastBackpackID() + 1);

        event.setResult(driedBackpack);
    }
}
