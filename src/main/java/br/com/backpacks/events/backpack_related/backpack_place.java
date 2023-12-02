package br.com.backpacks.events.backpack_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

public class backpack_place implements Listener {

    @EventHandler
    private void general_place_event(BlockPlaceEvent event){
        if(!event.getPlayer().isSneaking()) return;
        if(!event.getBlockPlaced().getType().equals(Material.CHEST)) return;
        if(!event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())) return;

        BackPack backPack = Main.backPackManager.get_backpack_from_id(event.getItemInHand().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        Main.backPackManager.getBackpacks_placed_locations().put(event.getBlockPlaced().getLocation(), backPack);
    }

}
