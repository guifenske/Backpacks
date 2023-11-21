package br.com.Backpacks.events.backpack_related;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
import br.com.Backpacks.recipes.Recipes;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class backpack_interact implements Listener {

    @EventHandler
    private void general_interaction_event(PlayerInteractEvent event){
        if(event.getAction().equals(RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CHEST)){
            if(!event.getPlayer().isSneaking()) return;
            if(event.getItem() != null) return;

            if(Main.back.backPackManager.getBackpacks_placed_locations().containsKey(event.getClickedBlock().getLocation())){
                BackPack backPack = Main.back.backPackManager.getBackpacks_placed_locations().get(event.getClickedBlock().getLocation());
                event.setCancelled(true);
                backPack.open(event.getPlayer());
            }
            return;
        }
        if(event.getItem() == null) return;

        if(!event.getItem().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK())) return;

        BackPack backPack = Main.back.backPackManager.get_backpack_from_id(event.getPlayer(), event.getItem().getItemMeta().getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        event.setCancelled(true);
        backPack.open(event.getPlayer());
    }
}