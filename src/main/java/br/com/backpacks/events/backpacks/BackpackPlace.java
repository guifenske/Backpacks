package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.backpack.Backpack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class  BackpackPlace implements Listener {

    @EventHandler
    private void backpackPlaceEvent(BlockPlaceEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        if (!event.getBlockPlaced().getType().equals(Material.BARREL)) return;

        PersistentDataContainer itemData = event.getItemInHand().getItemMeta().getPersistentDataContainer();

        if (!itemData.has(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER)) {
            if (itemData.has(BackpackRecipes.NAMESPACE_WET_BACKPACK, PersistentDataType.INTEGER)) {
                event.getPlayer().sendMessage(Main.getMain().PREFIX + "§cHumm, this thing is to wet to be used as a backpack.");
                event.setCancelled(true);
            }
            return;
        }

        if(!Main.backpackManager.canOpen()){
            event.setCancelled(true);
            return;
        }

        Backpack backpack = Main.backpackManager.getBackpackFromId(itemData.get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER));
        if (backpack == null) return;

        //enforce removal of the item from the player's inventory
        //for some reason, Inventory.remove() doesn't remove from offHand slot.

        //TODO: cleanup logic
        if(event.getPlayer().getInventory().getItemInOffHand().hasItemMeta() && event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BARREL)){

            if(event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK, PersistentDataType.INTEGER)){
                int id = event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER);

                if(backpack.getId() == id){
                    event.getPlayer().getInventory().setItemInOffHand(null);
                }
            }

        }

        event.getPlayer().getInventory().remove(event.getItemInHand());
        backpack.setIsBlock(true);
        backpack.setOwner(null);
        Location backpackLocation = event.getBlockPlaced().getLocation();
        backpack.setLocation(backpackLocation);

        //we need to do this to trigger the hopper event
        backpack.updateBarrelBlock();

        backpack.getConfigMenu().refreshMenu();

        Main.backpackManager.getBackpacksPlacedLocations().put(backpackLocation, backpack.getId());
    }
}
