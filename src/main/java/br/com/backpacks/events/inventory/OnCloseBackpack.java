package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OnCloseBackpack implements Listener {

    @EventHandler
    private void OnClose(InventoryCloseEvent event){
        if(!Main.backPackManager.isInBackpack.containsKey(event.getPlayer().getUniqueId())) return;
        Main.backPackManager.isInBackpack.remove(event.getPlayer().getUniqueId());
    }
}
