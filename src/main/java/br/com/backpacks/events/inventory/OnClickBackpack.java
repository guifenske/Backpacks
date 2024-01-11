package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickBackpack implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.OPENED)) return;

        Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() == event.getInventory().getSize() - 1){
            event.setCancelled(true);
            BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
            player.openInventory(InventoryBuilder.mainConfigInv(player));
            BackpackAction.setAction(player, BackpackAction.Action.CONFIGMENU);
            event.setCancelled(true);
        }   else if(event.getRawSlot() == event.getInventory().getSize() - 2){
            event.setCancelled(true);
            BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
            if(backPack == null)    return;

            if(backPack.getSecondPageSize() == 0) return;

            switch (Main.backPackManager.getCurrentPage().get(player.getUniqueId())) {
                case 1 ->{
                    event.setCancelled(true);
                    backPack.openSecondPage(player);
                    event.setCancelled(true);
                }
                case 2 -> {
                    event.setCancelled(true);
                    backPack.open(player);
                    event.setCancelled(true);
                }
            }

        }
    }
}
