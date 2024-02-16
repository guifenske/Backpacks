package br.com.backpacks.events;

import br.com.backpacks.recipes.UpgradesRecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.persistence.PersistentDataType;

public class ConfigItemsEvents implements Listener {

    @EventHandler
    private void onPlace(BlockPlaceEvent event){
        if(event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent event){
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onItemDispense(BlockDispenseEvent event){
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onUse(PlayerInteractEvent event){
        if(event.getItem() == null) return;
        if(event.getItem().getType().equals(Material.BUCKET) && event.getItem().hasItemMeta()){
            if(event.getItem().getItemMeta().getPersistentDataContainer().has(new UpgradesRecipesNamespaces().isUpgrade(), PersistentDataType.INTEGER)){
                event.setCancelled(true);
            }
        }
    }
}
