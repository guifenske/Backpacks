package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickBackpack implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getActions(event.getWhoClicked()).contains(BackpackAction.Action.OPENED)) return;
        Player player = (Player) event.getWhoClicked();
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(player);
        if(backPack == null) return;

        if(event.getRawSlot() == event.getInventory().getSize() - 1){
            event.setCancelled(true);
            BackpackAction.getActions(player).remove(BackpackAction.Action.OPENED);
            Main.backPackManager.getCurrentPage().remove(player.getUniqueId());
            player.openInventory(InventoryBuilder.getConfigInv(backPack));
            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> BackpackAction.addAction(player, BackpackAction.Action.CONFIGMENU), 1L);
        }   else if(event.getRawSlot() == event.getInventory().getSize() - 2){
            if(backPack.getSecondPageSize() == 0) return;
            event.setCancelled(true);

            switch (Main.backPackManager.getCurrentPage().get(player.getUniqueId())) {
                case 1 ->{
                    BackpackAction.getActions(player).remove(BackpackAction.Action.OPENED);
                    backPack.openSecondPage(player);
                    event.setCancelled(true);
                }
                case 2 -> {
                    BackpackAction.getActions(player).remove(BackpackAction.Action.OPENED);
                    backPack.open(player);
                    event.setCancelled(true);
                }
            }

        }
    }
}
