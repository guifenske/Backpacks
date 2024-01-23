package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeManager;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class OnClickIOMenu implements Listener {
    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(BackpackAction.getAction(event.getWhoClicked()) != BackpackAction.Action.IOMENU) return;
        event.setCancelled(true);
        switch (event.getRawSlot()){
            case 11 -> {
                BackpackAction.removeAction((Player) event.getWhoClicked());
                event.getWhoClicked().openInventory(previewInventory((Player) event.getWhoClicked(), Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()))));
                BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.EDITINGINPUT);
            }
            case 15 -> {
                BackpackAction.removeAction((Player) event.getWhoClicked());
                event.getWhoClicked().openInventory(previewInventory((Player) event.getWhoClicked(), Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()))));
                BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.EDITINGOUTPUT);
            }

            case 22 ->{
                BackpackAction.removeAction((Player) event.getWhoClicked());
                event.getWhoClicked().openInventory(InventoryBuilder.mainConfigInv((Player) event.getWhoClicked()));
                BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.CONFIGMENU);
            }
        }
    }

    @EventHandler
    private void onClickEditing(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(BackpackAction.getAction(event.getWhoClicked()) != BackpackAction.Action.EDITINGINPUT && BackpackAction.getAction(event.getWhoClicked()) != BackpackAction.Action.EDITINGOUTPUT) return;
        event.setCancelled(true);
        if(event.getRawSlot() >= InventoryBuilder.getFreeUpgradesSlots(Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId())).getType())) return;

        Upgrade upgrade = RecipesUtils.getUpgradeFromItem(event.getCurrentItem(), Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId())));
        if(upgrade == null) return;
        if(BackpackAction.getAction(event.getWhoClicked()) == BackpackAction.Action.EDITINGINPUT) {
            Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId())).setUpgradeInputId(upgrade.getId());
        }   else{
            Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId())).setUpgradeOutputId(upgrade.getId());
        }

        BackpackAction.removeAction((Player) event.getWhoClicked());
        event.getWhoClicked().openInventory(InventoryBuilder.inputOutputMenu((Player) event.getWhoClicked()));
        BackpackAction.setAction((Player) event.getWhoClicked(), BackpackAction.Action.IOMENU);

    }

    private Inventory previewInventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, 9, "Click on the desired Upgrade");
        ItemStack item = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").get();

        for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 9; i++){
            inventory.setItem(i, item);
        }

        int i = 0;
        Set<Upgrade> upgrades = backPack.getUpgrades();
        if(backPack.getUpgrades() != null) {
            if (!backPack.getUpgrades().isEmpty()) {
                for (Upgrade upgrade : upgrades) {
                    if(!UpgradeManager.upgradeInventoryIsAdvanced(upgrade)){
                        i++;
                        continue;
                    }
                    inventory.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                    i++;
                }
            }
        }

        return inventory;
    }
}
