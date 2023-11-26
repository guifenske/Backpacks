package br.com.Backpacks.events.backpack_related;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
import br.com.Backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class backpack_interact implements Listener {

    @EventHandler
    private void general_interaction_event(PlayerInteractEvent event){
        if(event.getAction().equals(RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CHEST)){
            if(event.getPlayer().isSneaking()) return;

            if(Main.back.backPackManager.get_backpack_from_location(event.getClickedBlock().getLocation()) == null) return;

            BackPack backPack = Main.back.backPackManager.get_backpack_from_location(event.getClickedBlock().getLocation());
            event.getPlayer().closeInventory();
            event.setCancelled(true);
            backPack.open(event.getPlayer());
            return;
        }
        if(!event.getAction().equals(RIGHT_CLICK_BLOCK) && !event.getAction().equals(RIGHT_CLICK_AIR)) return;
        if(event.getItem() == null) return;
        if(event.getPlayer().isSneaking() && event.getAction().equals(RIGHT_CLICK_BLOCK)) return;
        if(!event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())) return;

        BackPack backPack = Main.back.backPackManager.get_backpack_from_id(event.getItem().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        event.setCancelled(true);
        backPack.open(event.getPlayer());
    }
}