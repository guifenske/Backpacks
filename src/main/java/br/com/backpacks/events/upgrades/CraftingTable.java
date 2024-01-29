package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CraftingTable implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!event.getInventory().getType().equals(InventoryType.WORKBENCH)) return;
        if(BackpackAction.getAction((Player) event.getPlayer()) != BackpackAction.Action.UPGCRAFTINGGRID) return;

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        if(backPack == null) return;

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }
}
