package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import br.com.backpacks.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if(BackpackAction.getAction(player).equals(BackpackAction.Action.NOTHING) || BackpackAction.getAction(player).equals(BackpackAction.Action.OPENED)) return;

        int slot = event.getRawSlot();
        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(player);

        Menu menu = backpack.getPlayerCurrentMenu(player);
        if(menu == null){
            //may cancel the event when the process of migrating the upgrades inventories to menus is completed
            return;
        }

        if(slot > menu.getSize()){
            menu.onClickBottomInventory(player, event);
            return;
        }

        if(menu.getButton(slot) == null) return;

        event.setCancelled(true);
        menu.getButton(slot).onClick(player);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.NOTHING) || BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.OPENED)) return;

        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(event.getPlayer());
        if(backpack == null) return;

        Menu menu = backpack.getPlayerCurrentMenu((Player) event.getPlayer());

        if(menu == null) return;

        menu.onClose((Player) event.getPlayer());
    }

}
