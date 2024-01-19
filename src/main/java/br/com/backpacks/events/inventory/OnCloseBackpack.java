package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class OnCloseBackpack implements Listener {

    @EventHandler
    private void OnClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.OPENED)) return;
        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        shouldRemoveBackpack(event, backPack);

        Main.backPackManager.getCurrentPage().remove(event.getPlayer().getUniqueId());
        Main.backPackManager.getCurrentBackpackId().remove(event.getPlayer().getUniqueId());
    }

    private void shouldRemoveBackpack(InventoryCloseEvent event, BackPack backPack) {
        for(ItemStack itemStack : backPack.getFirstPage()){
            if(itemStack == null) continue;
            if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK()) && !backPack.containsUpgradeType(UpgradeType.ENCAPSULATE)){
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
                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK()) && !backPack.containsUpgradeType(UpgradeType.ENCAPSULATE)){
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
