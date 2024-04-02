package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CraftingTable implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGCRAFTINGGRID)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        if(backPack == null) return;

        BackpackAction.clearPlayerAction(event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }
}
