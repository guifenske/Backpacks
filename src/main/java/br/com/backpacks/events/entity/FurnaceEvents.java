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

        RandomBackpackBuilder randomBackpackBuilder = new RandomBackpackBuilder("Unknown Backpack", Main.backpackManager.getLastBackpackID() + 1);
        Backpack backpack = randomBackpackBuilder.generateBackpack();

        Main.backpackManager.setLastBackpackID(Main.backpackManager.getLastBackpackID() + 1);
        event.setResult(backpack.getBackpackItem());
    }
}
