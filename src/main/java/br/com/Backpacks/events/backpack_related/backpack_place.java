package br.com.Backpacks.events.backpack_related;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
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

        ItemMeta meta = event.getItemInHand().getItemMeta();

        if (meta.getPersistentDataContainer().has(new Recipes().getNAMESPACE_BACKPACK_ID())) {
            int id = meta.getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
            BackPack backPack = Main.back.backPackManager.get_backpack_from_id(event.getPlayer(), id);

            for(BackPack backPack1 : Main.back.backPackManager.getPlayerBackPacks(event.getPlayer())){
                event.getPlayer().openInventory(backPack1.getCurrent_page());
                event.setCancelled(true);
                return;
            }

            if(backPack == null){
                return;
            }

            event.setCancelled(true);
        }
    }
}
