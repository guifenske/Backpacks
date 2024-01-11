package br.com.backpacks.events.backpack_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BackpackPlace implements Listener {

    @EventHandler
    private void generalPlaceEvent(BlockPlaceEvent event){
        if(!event.getPlayer().isSneaking()) return;
        if(!event.getBlockPlaced().getType().equals(Material.CHEST)) return;
        
        PersistentDataContainer itemData = event.getItemInHand().getItemMeta().getPersistentDataContainer();
        if(!itemData.has(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID())){
            if(itemData.has(new RecipesNamespaces().getIS_BACKPACK())){
                event.getPlayer().sendMessage(Main.PREFIX + "§cHumm, this thing is to wet to be used as a backpack.");
                event.setCancelled(true);
            }
            return;
        }

        BackPack backPack = Main.backPackManager.getBackpackFromId(itemData.get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if(backPack == null) return;
        //enforce removal of the item from the player's inventory
        event.getPlayer().getInventory().remove(event.getItemInHand());

        backPack.setIsBlock(true);

        Location backpackLocation = event.getBlockPlaced().getLocation();
        Main.backPackManager.getBackpacksPlacedLocations().put(backpackLocation, backPack.getId());
        backPack.setLocation(backpackLocation);
    }

}
