package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class CraftingGrid implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!event.getInventory().getType().equals(InventoryType.WORKBENCH)) return;
        if(BackpackAction.getAction((Player) event.getPlayer()) != BackpackAction.Action.UPGCRAFTINGGRID) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        if(backPack == null) return;

        backPack.open((Player) event.getPlayer());
    }
}
