package br.com.Backpacks.events.backpack_related;

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
    private void place_event(BlockPlaceEvent event) {
        if (!event.getItemInHand().getType().equals(org.bukkit.Material.CHEST)) {
            return;
        }

        if (!event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK()))
            return;

        ItemMeta meta = event.getItemInHand().getItemMeta();

        if (meta.getPersistentDataContainer().has(new Recipes().getNAMESPACE_BACKPACK_ID())) {
            int id = meta.getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
            BackPack backPack = Main.back.backPackManager.get_backpack_from_id(event.getPlayer(), id);

            if(backPack == null){
                return;
            }

            event.getPlayer().openInventory(backPack.getCurrent_page());

            event.setCancelled(true);
        }
    }
}
