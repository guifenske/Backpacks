package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OnCloseBackpackConfigMenu implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!Main.backPackManager.isInBackpackConfig.containsKey(event.getPlayer().getUniqueId())) return;
        if(Main.backPackManager.isInBackpack.containsKey(event.getPlayer().getUniqueId())) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isInBackpackConfig.get(event.getPlayer().getUniqueId()));
        if(backPack == null) return;

        Main.backPackManager.isInBackpackConfig.remove(event.getPlayer().getUniqueId());
        backPack.open((Player) event.getPlayer());
    }
}
