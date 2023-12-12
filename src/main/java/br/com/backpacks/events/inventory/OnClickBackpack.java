package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class OnClickBackpack implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.OPENED)) return;

        Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() == event.getInventory().getSize() - 1){
            event.setCancelled(true);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    BackpackAction.setAction(player, BackpackAction.Action.CONFIGMENU);
                    player.openInventory(InventoryBuilder.mainConfigInv(player));
                    event.setCancelled(true);
                }
            }.runTaskLater(Main.getMain(), 1L);
        }   else if(event.getRawSlot() == event.getInventory().getSize() - 2){
            event.setCancelled(true);
            BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
            if(backPack == null)    return;

            if(backPack.getSecondPageSize() == 0) return;

            BackpackAction.setAction(player, BackpackAction.Action.NOTHING);

            switch (Main.backPackManager.getCurrentPage().get(player.getUniqueId())) {
                case 1 ->{
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            backPack.openSecondPage(player);
                            event.setCancelled(true);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                }
                case 2 -> {
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            backPack.open(player);
                            player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1, 1);
                            event.setCancelled(true);
                        }
                    }.runTaskLater(Main.getMain(), 1L);
                }
            }

        }
    }
}
