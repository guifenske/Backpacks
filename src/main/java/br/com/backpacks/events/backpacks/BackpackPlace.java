package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BackpackPlace implements Listener {

    @EventHandler
    private void generalPlaceEvent(BlockPlaceEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        if (!event.getBlockPlaced().getType().equals(Material.BARREL)) return;

        PersistentDataContainer itemData = event.getItemInHand().getItemMeta().getPersistentDataContainer();
        if (!itemData.has(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID())) {
            if (itemData.has(new RecipesNamespaces().getNAMESPACE_WET_BACKPACK())) {
                event.getPlayer().sendMessage(Main.PREFIX + "§cHumm, this thing is to wet to be used as a backpack.");
                event.setCancelled(true);
            }
            return;
        }

        if(Main.backPackManager.canOpen()) {
            BackPack backPack = Main.backPackManager.getBackpackFromId(itemData.get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
            if (backPack == null) return;
            //enforce removal of the item from the player's inventory
            event.getPlayer().getInventory().remove(event.getItemInHand());
            backPack.setIsBlock(true);
            backPack.setOwner(null);
            Location backpackLocation = event.getBlockPlaced().getLocation();
            backPack.setLocation(backpackLocation);
            //we need to do this to trigger the hopper event
            Barrel barrel = (Barrel) event.getBlockPlaced().getState();
            barrel.getInventory().setItem(0, new ItemStack(Material.STICK));

            InventoryBuilder.updateConfigInv(backPack);
            Main.backPackManager.getBackpacksPlacedLocations().put(backpackLocation, backPack.getId());
        }   else event.setCancelled(true);
    }
}
