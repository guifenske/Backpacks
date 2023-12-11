package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackpackAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OnCloseBackpack implements Listener {

    @EventHandler
    private void OnClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.OPENED)) return;
        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);

        Main.backPackManager.getCurrentBackpackId().remove(event.getPlayer().getUniqueId());
    }
}
