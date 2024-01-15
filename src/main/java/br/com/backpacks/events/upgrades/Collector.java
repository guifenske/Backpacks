package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Collector implements Listener {

    @EventHandler
    private void onPickUp(PlayerAttemptPickupItemEvent event){
        if(!event.getPlayer().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack == null) return;
        if(!backPack.isCollectorEnabled()) return;
        if(backPack.getCollectorMode() == 0){
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
        }   else {
            event.setCancelled(true);
            event.getPlayer().playPickupItemAnimation(event.getItem());
            event.getItem().remove();
        }
    }


    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, 27, "§6Collector: mode " + backPack.getCollectorMode());
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();
        ItemStack mode0 = new ItemCreator(Material.LIME_STAINED_GLASS_PANE, "Mode 0: Only pickup backpack stored items").get();
        ItemStack mode1 = new ItemCreator(Material.BLUE_STAINED_GLASS_PANE, "Mode 1: Store all pickup-ed items into the backpack").get();

        if(backPack.isCollectorEnabled()) {
            inventory.setItem(11, disable);
        }   else {
            inventory.setItem(11, enable);
        }

        if(backPack.getCollectorMode() == 0){
            inventory.setItem(13, mode1);
        }   else {
            inventory.setItem(13, mode0);
        }
        return inventory;
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGCOLLECTOR)){
            event.setCancelled(true);
            BackPack backpack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
            if(backpack == null) return;

            switch (event.getRawSlot()){
                case 11 -> {
                    backpack.setCollectorIsEnabled(!backpack.isCollectorEnabled());
                    if(backpack.isCollectorEnabled()){
                        event.getInventory().setItem(11, new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get());
                    }   else{
                        event.getInventory().setItem(11, new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get());
                    }
                }
                case 13 -> {
                    backpack.setCollectorMode(backpack.getCollectorMode() == 0 ? 1 : 0);
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openInventory(inventory((Player) event.getWhoClicked(), backpack));
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGCOLLECTOR);
                }
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGCOLLECTOR)){
            BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
            BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> backPack.open((Player) event.getPlayer()), 1L);
        }
    }

}
