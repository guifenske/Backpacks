package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.inventory.InventoryBuilder;
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
        if (!itemData.has(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER)) {
            if (itemData.has(BackpackRecipes.getNAMESPACE_WET_BACKPACK(), PersistentDataType.INTEGER)) {
                event.getPlayer().sendMessage(Main.PREFIX + "§cHumm, this thing is to wet to be used as a backpack.");
                event.setCancelled(true);
            }
            return;
        }

        if(!Main.backPackManager.canOpen()){
            event.setCancelled(true);
            return;
        }

        BackPack backPack = Main.backPackManager.getBackpackFromId(itemData.get(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if (backPack == null) return;

        //enforce removal of the item from the player's inventory
        //for some reason, Inventory.remove() doesn't remove from offHand slot.
        if(event.getPlayer().getInventory().getItemInOffHand().hasItemMeta() && event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BARREL)){
            if(event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.isBackpack(), PersistentDataType.INTEGER)){
                int id = event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                if(backPack.getId() == id){
                    event.getPlayer().getInventory().setItemInOffHand(null);
                }
            }
        }

        event.getPlayer().getInventory().remove(event.getItemInHand());
        backPack.setIsBlock(true);
        backPack.setOwner(null);
        Location backpackLocation = event.getBlockPlaced().getLocation();
        backPack.setLocation(backpackLocation);

        //we need to do this to trigger the hopper event
        backPack.updateBarrelBlock();

        InventoryBuilder.updateConfigInv(backPack);
        Main.backPackManager.getBackpacksPlacedLocations().put(backpackLocation, backPack.getId());
    }
}
