package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickConfig implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!Main.backPackManager.isInBackpack.containsKey(event.getWhoClicked().getUniqueId())) return;
        if(event.getSlot() != event.getClickedInventory().getSize() - 1) return;

        event.setCancelled(true);
        Main.backPackManager.isInBackpackConfig.put(event.getWhoClicked().getUniqueId(), Main.backPackManager.isInBackpack.get(event.getWhoClicked().getUniqueId()));
        event.getWhoClicked().openInventory(InventoryBuilder.mainConfigInv((Player) event.getWhoClicked()));
        Main.backPackManager.isInBackpack.remove(event.getWhoClicked().getUniqueId());
    }
}
