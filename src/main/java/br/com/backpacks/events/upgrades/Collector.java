package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.CollectorUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Collector implements Listener {
    public static final HashMap<UUID, CollectorUpgrade> currentCollector = new HashMap<>();

    @EventHandler
    private void onPickUp(PlayerAttemptPickupItemEvent event){
        if(!event.getPlayer().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;
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
        if(!backPack.tryAddItem(event.getItem().getItemStack()).isEmpty()){
            List<ItemStack> list = backPack.getRemainingItems();
            for(ItemStack itemStack : list){
                event.getItem().setItemStack(itemStack);
            }
            backPack.setRemainingItems(null);
        }   else {
            event.setCancelled(true);
            event.getPlayer().playPickupItemAnimation(event.getItem());
            event.getItem().remove();
        }
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGCOLLECTOR)){
            event.setCancelled(true);
            BackPack backpack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
            boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK());
            if(canUse) canUse = backpack.getId() == event.getWhoClicked().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER);
            if(!canUse){
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(Main.PREFIX + "§cYou can't use this upgrade because this backpack is not in your back.");
                return;
            }
            CollectorUpgrade upgrade = currentCollector.get(event.getWhoClicked().getUniqueId());

            switch (event.getRawSlot()){
                case 11 -> {
                    upgrade.setEnabled(!upgrade.isEnabled());
                    upgrade.updateInventory();
                }
                case 13 -> {
                    upgrade.setMode(upgrade.getMode() == 0 ? 1 : 0);
                    upgrade.updateInventory();
                }
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGCOLLECTOR)){
            BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
            BackpackAction.removeAction((Player) event.getPlayer());
            currentCollector.get(event.getPlayer().getUniqueId()).getViewers().remove((Player) event.getPlayer());
            currentCollector.remove(event.getPlayer().getUniqueId());
            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> backPack.open((Player) event.getPlayer()), 1L);
        }
    }

}
