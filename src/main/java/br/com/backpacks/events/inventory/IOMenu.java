package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.backpacks.BackpackManager;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class IOMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.IOMENU)) return;
        BackPack backPack = BackpackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        event.setCancelled(true);

        switch (event.getRawSlot()){
            case 11 -> {
                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                    event.getWhoClicked().openInventory(InventoryBuilder.getEditIOMenu(backPack));
                    BackpackAction.setAction(event.getWhoClicked(), BackpackAction.Action.EDITINPUT);
                }, 1L);
            }

            case 13 -> {
                backPack.setInputUpgrade(-1);
                backPack.setOutputUpgrade(-1);
                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> event.getWhoClicked().closeInventory(), 1L);
            }

            case 15 -> {
                Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> {
                    event.getWhoClicked().openInventory(InventoryBuilder.getEditIOMenu(backPack));
                    BackpackAction.setAction(event.getWhoClicked(), BackpackAction.Action.EDITOUTPUT);
                }, 1L);
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.IOMENU)) return;
        BackPack backPack = BackpackManager.getPlayerCurrentBackpack(event.getPlayer());
        BackpackAction.clearPlayerAction(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open((Player) event.getPlayer());
        }, 1L);
    }

    @EventHandler
    private void onClickEditMenu(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.EDITOUTPUT) && !BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.EDITINPUT)) return;
        event.setCancelled(true);
        if(event.getRawSlot() > 8) return;
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

        BackPack backPack = BackpackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        Upgrade upgrade = RecipesUtils.getUpgradeFromItem(event.getCurrentItem());

        if(BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.EDITOUTPUT)){
            backPack.setOutputUpgrade(upgrade.getId());
            Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> event.getWhoClicked().closeInventory(), 1L);
            return;
        }

        backPack.setInputUpgrade(upgrade.getId());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> event.getWhoClicked().closeInventory(), 1L);
    }

    @EventHandler
    private void onCloseEditMenu(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.EDITOUTPUT) && !BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.EDITINPUT)) return;
        BackPack backPack = BackpackManager.getPlayerCurrentBackpack(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open((Player) event.getPlayer());
        }, 1L);
    }

}
