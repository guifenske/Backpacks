package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.backpackUtils.inventory.UpgradeMenu;
import br.com.backpacks.events.upgrades.*;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.RecipesUtils;
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
        event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;

        if(event.getRawSlot() < InventoryBuilder.getFreeUpgradesSlots(backPack.getType())){
            if(event.getCurrentItem() == null) return;
            Upgrade upgrade = RecipesUtils.getUpgradeFromItem(event.getCurrentItem());
            if(upgrade == null) return;

            switch (upgrade) {
                case CRAFTING -> {
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openWorkbench(null, true);
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGCRAFTINGGRID);
                    event.setCancelled(true);
                }

                case FURNACE -> {
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openInventory(Furnace.inventory((Player) event.getWhoClicked(), backPack));
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGFURNACE);
                    event.setCancelled(true);
                }

                case JUKEBOX -> {
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openInventory(Jukebox.inventory((Player) event.getWhoClicked(), backPack));
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGJUKEBOX);
                    event.setCancelled(true);
                }

                case AUTOFEED -> {
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openInventory(AutoFeed.inventory((Player) event.getWhoClicked(), backPack));
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGAUTOFEED);
                    event.setCancelled(true);
                }

                case VILLAGERSFOLLOW -> {
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openInventory(VillagersFollow.inventory((Player) event.getWhoClicked(), backPack));
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGVILLAGERSFOLLOW);
                    event.setCancelled(true);
                }

                case COLLECTOR -> {
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.NOTHING);
                    event.getWhoClicked().openInventory(Collector.inventory((Player) event.getWhoClicked(), backPack));
                    BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.UPGCOLLECTOR);
                    event.setCancelled(true);
                }
            }

            return;
        }

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()) {
            //go back to the previous page
            case 45 -> player.closeInventory();
            //equip or un-equip backpack in the back
            case 53 -> {
                if(backPack.isBlock())  return;

                if (player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) {
                    player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
                    backPack.setBeingWeared(false);
                } else {
                    player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
                    player.getPersistentDataContainer().set(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER, backPack.getId());
                    backPack.setBeingWeared(true);
                }

                player.closeInventory();
        }
            //rename backpack
            case 52 -> {
                BackpackAction.setAction(player, BackpackAction.Action.RENAMING);
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
            }

            case 51 -> {
                if (event.getClickedInventory().getItem(53) == null) return;
                backPack.setLocked(!backPack.isLocked());
                player.closeInventory();
            }

            case 36 ->{
                BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
                player.openInventory(UpgradeMenu.editUpgrades(player));
                BackpackAction.setAction(player, BackpackAction.Action.UPGMENU);
                event.setCancelled(true);
            }
        }
    }
}