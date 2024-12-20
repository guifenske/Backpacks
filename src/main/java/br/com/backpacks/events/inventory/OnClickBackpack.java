package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickBackpack implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.OPENED)) return;
        Player player = (Player) event.getWhoClicked();
        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(player);
        if(backpack == null) return;

        if(event.getRawSlot() == event.getInventory().getSize() - 1){
            event.setCancelled(true);
            BackpackAction.clearPlayerAction(player);

            Main.backpackManager.clearPlayerCurrentPage(player);

            backpack.getConfigMenu().displayTo(player);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> BackpackAction.setAction(player, BackpackAction.Action.CONFIGMENU), 1L);
        }

        else if(event.getRawSlot() == event.getInventory().getSize() - 2){
            if(backpack.getSecondPageSize() == 0) return;
            event.setCancelled(true);

            switch (Main.backpackManager.getPlayerCurrentPage(player)) {
                case 1 ->{
                    BackpackAction.clearPlayerAction(player);
                    backpack.openSecondPage(player);
                    event.setCancelled(true);
                }

                case 2 -> {
                    BackpackAction.clearPlayerAction(player);
                    backpack.open(player);
                    event.setCancelled(true);
                }
            }
        }   else{
            Bukkit.getScheduler().runTaskLater(Main.getMain(), backpack::updateBarrelBlock, 1L);
        }
    }
}
