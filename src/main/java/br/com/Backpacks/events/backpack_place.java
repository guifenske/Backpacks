package br.com.Backpacks.events;

import br.com.Backpacks.recipes.Recipes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class backpack_place implements Listener {

    @EventHandler
    private void place_event(BlockPlaceEvent event){
        if(!event.getItemInHand().getType().equals(org.bukkit.Material.CHEST)){
            return;
        }

        if(!event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK())) return;

        new Recipes().create_test_backpack(event.getPlayer());

        event.setCancelled(true);
    }
}
