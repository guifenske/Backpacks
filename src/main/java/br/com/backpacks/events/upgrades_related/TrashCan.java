package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TrashCan implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.OPENED)) return;
        if(event.getRawSlot() != event.getInventory().getSize() - 3) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;
        if(!backPack.containsUpgrade(Upgrade.TRASH)) return;

        ItemStack currentItem = event.getCurrentItem();
        ItemStack newItem = event.getWhoClicked().getItemOnCursor();

        if (currentItem != null) {
            event.getWhoClicked().setItemOnCursor(null);
            event.setCurrentItem(null);
            if (newItem.isEmpty()) {
                event.getWhoClicked().getInventory().addItem(currentItem);
                return;
            }
            event.setCurrentItem(newItem);
        }
    }
}
