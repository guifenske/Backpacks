package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.UpgradeType;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class OnCloseBackpack implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.OPENED)) return;
        BackpackAction.clearPlayerAction(event.getPlayer());

        Player player = (Player) event.getPlayer();

        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(event.getPlayer());
        shouldRemoveBackpack(event, backpack);

        backpack.getViewersIds().remove(event.getPlayer().getUniqueId());
        if(backpack.getViewersIds().isEmpty()){
            if(backpack.getLocation() != null){
                Barrel barrel = (Barrel) backpack.getLocation().getBlock().getState();
                barrel.close();
            }
        }

        Main.backpackManager.clearPlayerCurrentPage(player);
        Main.backpackManager.clearPlayerCurrentBackpack(player);
    }

    private void shouldRemoveBackpack(InventoryCloseEvent event, Backpack backpack) {
        for(ItemStack itemStack : backpack.getFirstPage()){
            if(itemStack == null) continue;
            if(!itemStack.hasItemMeta()) continue;

            if(itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK) && !backpack.containsUpgradeType(UpgradeType.ENCAPSULATE)){

                if(!event.getPlayer().getInventory().addItem(itemStack).isEmpty()){
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                    event.getPlayer().sendMessage("§cYour inventory is full, the backpack was dropped on the ground.");
                }

                backpack.getFirstPage().remove(itemStack);
            }
        }

        if(backpack.getSecondPage() != null){
            for(ItemStack itemStack : backpack.getSecondPage()){

                if(itemStack == null) continue;
                if(!itemStack.hasItemMeta()) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK) && !backpack.containsUpgradeType(UpgradeType.ENCAPSULATE)){

                    if(!event.getPlayer().getInventory().addItem(itemStack).isEmpty()){
                        event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                        event.getPlayer().sendMessage("§cYour inventory is full, the backpack was dropped on the ground.");
                    }

                    backpack.getSecondPage().remove(itemStack);
                }

            }
        }
    }
}
