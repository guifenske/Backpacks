package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class HopperEvents implements Listener {

    @EventHandler
    private void hopperTick(HopperInventorySearchEvent event){
        if(event.getInventory() == null) return;
        if(!Main.backPackManager.getBackpacksPlacedLocations().containsKey(event.getSearchBlock().getLocation())) return;
        BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getSearchBlock().getLocation());

        if(backPack.getSecondPage() != null){
            if(backPack.getStorageContentsFirstPageWithoutNulls().size() > backPack.getConfigItemsSpace()){
                event.setInventory(backPack.getFirstPage());
            }
            else if(backPack.getStorageContentsSecondPageWithoutNulls().size() > backPack.getConfigItemsSpace()){
                event.setInventory(backPack.getSecondPage());
            }   else{
                event.setInventory(backPack.getFirstPage());
            }
            return;
        }

        event.setInventory(backPack.getFirstPage());
    }

    @EventHandler
    private void moveItem(InventoryMoveItemEvent event){
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_CONFIG_ITEM())){
            event.setCancelled(true);
        }
    }
}
