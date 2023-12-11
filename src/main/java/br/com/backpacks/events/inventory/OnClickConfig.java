package br.com.backpacks.events.inventory;

import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickConfig implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.OPENED)) return;
        if(event.getRawSlot() != event.getInventory().getSize() - 1) return;

        event.setCancelled(true);
        BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.CONFIGMENU);
        event.getWhoClicked().openInventory(InventoryBuilder.mainConfigInv((Player) event.getWhoClicked()));
    }
}
