package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.CONFIGMENU)) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;

        //just cancel the necessary clicks/events
        if(event.getRawSlot() >= InventoryBuilder.getFreeInitialSlots(backPack.getType()) && event.getRawSlot() < event.getInventory().getSize()) event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()) {
            //go back to the previous page
            case 45 -> player.closeInventory();
            //equip or un-equip backpack in the back
            case 53 -> {
                if (event.getClickedInventory().getItem(53) == null) return;

                if (player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) {
                    player.getInventory().addItem(Utils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
                } else {
                    player.getInventory().remove(Utils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().set(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER, Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
                }

                player.closeInventory();
        }
            //rename backpack
            case 52 -> {
                BackpackAction.setAction(player, BackpackAction.Action.RENAMING);
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
            }
        }
    }

}

