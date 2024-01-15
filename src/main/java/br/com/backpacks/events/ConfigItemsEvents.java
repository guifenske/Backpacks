package br.com.backpacks.events;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConfigItemsEvents implements Listener {

    @EventHandler
    private void onPlace(BlockPlaceEvent event){
        if(event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_CONFIG_ITEM())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent event){
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_CONFIG_ITEM())){
            event.setCancelled(true);
        }
    }


}
