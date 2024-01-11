package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

public class HopperEvents implements Listener {

    @EventHandler
    private void hopperTick(HopperInventorySearchEvent event){
        if(event.getInventory() == null) return;
        if(!event.getInventory().getType().equals(InventoryType.CHEST)) return;
        if(Main.backPackManager.getBackpackFromLocation(event.getSearchBlock().getLocation()) == null) return;

        BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getSearchBlock().getLocation());
        event.setInventory(backPack.getFirstPage());
    }

    @EventHandler
    private void moveItem(InventoryMoveItemEvent event){
       if(event.getSource().getType().equals(InventoryType.CHEST)){
           if(event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_CONFIG_ITEM())){
               event.setCancelled(true);
           }
       }
    }
}
