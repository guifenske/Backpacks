package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.CollectorUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Collector implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void onPickUp(PlayerAttemptPickupItemEvent event){
        if(!event.getPlayer().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack == null) return;
        List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.COLLECTOR);
        if(list.isEmpty()) return;
        CollectorUpgrade upgrade = (CollectorUpgrade) list.get(0);

        if(!upgrade.isEnabled()) return;
        if(upgrade.getMode() == 0){
            if(!backPack.containsItem(event.getItem().getItemStack())) return;
            generalLogic(event, backPack);
        }   else{
            generalLogic(event, backPack);
        }
    }

    private void generalLogic(PlayerAttemptPickupItemEvent event, BackPack backPack) {
        List<ItemStack> list = backPack.tryAddItem(event.getItem().getItemStack());
        if(!list.isEmpty()){
            event.getItem().setItemStack(list.get(0));
        }   else {
            event.setCancelled(true);
            event.getPlayer().playPickupItemAnimation(event.getItem());
            event.getItem().remove();
        }
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(BackpackAction.getActions(event.getWhoClicked()).contains(BackpackAction.Action.UPGCOLLECTOR)){
            event.setCancelled(true);

            switch (event.getRawSlot()){
                case 11 -> {
                    BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.COLLECTOR);
                    CollectorUpgrade upgrade = (CollectorUpgrade) list.get(0);
                    upgrade.setEnabled(!upgrade.isEnabled());
                    upgrade.updateInventory();
                }
                case 13 -> {
                    BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.COLLECTOR);
                    CollectorUpgrade upgrade = (CollectorUpgrade) list.get(0);
                    upgrade.setMode(upgrade.getMode() == 0 ? 1 : 0);
                    upgrade.updateInventory();
                }
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getActions(event.getPlayer()).contains(BackpackAction.Action.UPGCOLLECTOR)){
            BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
            BackpackAction.clearPlayerActions(event.getPlayer());
            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> backPack.open((Player) event.getPlayer()), 1L);
        }
    }

}
