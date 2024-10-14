package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.block.Barrel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class OnCloseBackpack implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.OPENED)) return;
        BackpackAction.clearPlayerAction(event.getPlayer());

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        shouldRemoveBackpack(event, backPack);
        backPack.getViewersIds().remove(event.getPlayer().getUniqueId());
        if(backPack.getViewersIds().isEmpty()){
            if(backPack.getLocation() != null){
                Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
                barrel.close();
            }
        }

        Main.backPackManager.getCurrentPage().remove(event.getPlayer().getUniqueId());
        Main.backPackManager.getCurrentBackpackId().remove(event.getPlayer().getUniqueId());
    }

    private void shouldRemoveBackpack(InventoryCloseEvent event, BackPack backPack) {
        for(ItemStack itemStack : backPack.getFirstPage()){
            if(itemStack == null) continue;
            if(!itemStack.hasItemMeta()) continue;

            if(itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK) && !backPack.containsUpgradeType(UpgradeType.ENCAPSULATE)){

                if(!event.getPlayer().getInventory().addItem(itemStack).isEmpty()){
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                    event.getPlayer().sendMessage("§cYour inventory is full, the backpack was dropped on the ground.");
                }

                backPack.getFirstPage().remove(itemStack);
            }
        }

        if(backPack.getSecondPage() != null){
            for(ItemStack itemStack : backPack.getSecondPage()){

                if(itemStack == null) continue;
                if(!itemStack.hasItemMeta()) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_BACKPACK) && !backPack.containsUpgradeType(UpgradeType.ENCAPSULATE)){

                    if(!event.getPlayer().getInventory().addItem(itemStack).isEmpty()){
                        event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                        event.getPlayer().sendMessage("§cYour inventory is full, the backpack was dropped on the ground.");
                    }

                    backPack.getSecondPage().remove(itemStack);
                }

            }
        }
    }
}
