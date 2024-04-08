package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.FilterUpgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.backpacks.BackpackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FilterUpgradeEvents implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGFILTER)) return;
        FilterUpgrade upgrade = (FilterUpgrade) UpgradeManager.getPlayerCurrentUpgrade(event.getWhoClicked());
        if(event.getRawSlot() > 8) return;
        //apparently for versions above 1.19, event.getCursor() is NotNull, returning Material.isAir() to the item..

        if(upgrade.isAdvanced()){
            event.setCancelled(true);
            if(event.getCurrentItem() == null && event.getCursor() != null){
                if(event.getCursor().getType().isAir()) return;
                upgrade.getFilteredItems().add(event.getCursor().asOne());
                event.getInventory().setItem(event.getRawSlot(), event.getCursor().asOne());
            }
            if(event.getCurrentItem() != null && event.getCursor() == null){
                upgrade.getFilteredItems().remove(event.getCurrentItem().asOne());
                event.getInventory().setItem(event.getRawSlot(), null);
            }
            if(event.getCurrentItem() != null && event.getCursor() != null){
                if(event.getCursor().getType().isAir()){
                    upgrade.getFilteredItems().remove(event.getCurrentItem().asOne());
                    event.getInventory().setItem(event.getRawSlot(), null);
                    return;
                }
                upgrade.getFilteredItems().remove(event.getCurrentItem().asOne());
                upgrade.getFilteredItems().add(event.getCursor().asOne());
                event.getInventory().setItem(event.getRawSlot(), event.getCursor().asOne());
            }
            return;
        }

        if(event.getRawSlot() != 4){
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);

        if(event.getCurrentItem() == null && event.getCursor() != null){
            if(event.getCursor().getType().isAir()) return;
            upgrade.getFilteredItems().add(event.getCursor().asOne());
            event.getInventory().setItem(4, event.getCursor().asOne());
        }
        if(event.getCurrentItem() != null && event.getCursor() == null){
            upgrade.getFilteredItems().remove(event.getCurrentItem().asOne());
            event.getInventory().setItem(4, null);
        }
        if(event.getCurrentItem() != null && event.getCursor() != null){
            if(event.getCursor().getType().isAir()){
                upgrade.getFilteredItems().remove(event.getCurrentItem().asOne());
                event.getInventory().setItem(4, null);
                return;
            }
            upgrade.getFilteredItems().remove(event.getCurrentItem().asOne());
            upgrade.getFilteredItems().add(event.getCursor().asOne());
            event.getInventory().setItem(4, event.getCursor().asOne());
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGFILTER)) return;
        BackPack backPack = BackpackManager.getPlayerCurrentBackpack(event.getPlayer());

        BackpackAction.clearPlayerAction(event.getPlayer());
        UpgradeManager.removePlayerCurrentUpgrade(event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

}
