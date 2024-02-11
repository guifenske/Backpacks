package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickUpgradesMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGMENU)) return;

        Player player = (Player) event.getWhoClicked();

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));

        if(event.getRawSlot() >= InventoryBuilder.getFreeUpgradesSlots(backPack.getType()) && event.getRawSlot() < event.getInventory().getSize()){
            event.setCancelled(true);
        }
    }
}
