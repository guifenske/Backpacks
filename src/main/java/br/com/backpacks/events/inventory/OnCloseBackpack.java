package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.block.Barrel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class OnCloseBackpack implements Listener {

    @EventHandler
    private void OnClose(InventoryCloseEvent event){
        if(!BackpackAction.getActions(event.getPlayer()).contains(BackpackAction.Action.OPENED)) return;
        BackpackAction.clearPlayerActions(event.getPlayer());

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        shouldRemoveBackpack(event, backPack);
        backPack.getViewersIds().remove(event.getPlayer().getUniqueId());
        if(backPack.getViewersIds().isEmpty()){
            if(backPack.isBlock()){
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
            if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER) && !backPack.containsUpgradeType(UpgradeType.ENCAPSULATE)){
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
                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER) && !backPack.containsUpgradeType(UpgradeType.ENCAPSULATE)){
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
