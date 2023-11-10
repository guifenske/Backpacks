package br.com.Backpacks.events;

import br.com.Backpacks.BackPack;
import br.com.Backpacks.Main;
import br.com.Backpacks.recipes.Recipes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class backpack_place implements Listener {

    @EventHandler
    private void place_event(BlockPlaceEvent event){
        if(!event.getItemInHand().getType().equals(org.bukkit.Material.CHEST)){
            return;
        }

        if(!event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK())) return;

        ItemMeta meta = event.getItemInHand().getItemMeta();

        if(meta.getPersistentDataContainer().has(new Recipes().getNAMESPACE_BACKPACK_ID())){
            for(BackPack backPack : Main.back.backPackManager.getPlayerBackPacks(event.getPlayer())){
                if(backPack.getBackpack_id() == meta.getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER)){
                    event.getPlayer().openInventory(backPack.getCurrent_page());
                    break;
                }
            }
        }

        event.setCancelled(true);
    }
}
