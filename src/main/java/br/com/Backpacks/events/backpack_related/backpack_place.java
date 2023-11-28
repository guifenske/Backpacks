package br.com.Backpacks.events.backpack_related;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
import br.com.Backpacks.recipes.RecipesNamespaces;
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

        BackPack backPack = Main.back.backPackManager.get_backpack_from_id(event.getItemInHand().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        Main.back.backPackManager.getBackpacks_placed_locations().put(event.getBlockPlaced().getLocation(), backPack);
    }

}
