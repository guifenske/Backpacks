package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.TanksUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.inventory.InventoryAction.MOVE_TO_OTHER_INVENTORY;

public class Tanks implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGTANKS)) return;
        BackpackAction.removeAction((Player) event.getPlayer());
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open((Player) event.getPlayer());
        }, 1L);
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGTANKS)) return;
        if(event.getRawSlot() < 27 && event.getRawSlot() != 12 && event.getRawSlot() != 14) event.setCancelled(true);
        if(event.getRawSlot() > 27 && event.getAction().equals(MOVE_TO_OTHER_INVENTORY)) event.setCancelled(true);
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        if(backPack.getUpgradesFromType(UpgradeType.LIQUIDTANK).isEmpty()) return;
        TanksUpgrade tanksUpgrade = (TanksUpgrade) backPack.getUpgradesFromType(UpgradeType.LIQUIDTANK).get(0);

        switch (event.getRawSlot()){
            case 12, 14 -> Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
                generalLogic(tanksUpgrade);
            }, 1L);
        }
    }

    public void generalLogic(TanksUpgrade tanksUpgrade){
        for(int tank = 1; tank <= 2; tank++) {
            int index = 12;
            if(tank == 2) index = 14;
            ItemStack currentItem = tanksUpgrade.getInventory().getItem(index);
            if (currentItem == null) return;
            if (!currentItem.getType().toString().contains("BUCKET")) return;
            if (currentItem.getAmount() > 1) return;
            if (currentItem.hasItemMeta()) {
                if (!currentItem.getItemMeta().getDisplayName().equals(currentItem.getType().name()) || !currentItem.getItemMeta().getPersistentDataContainer().isEmpty())
                    return;
            }
            if (currentItem.getType().equals(Material.BUCKET)) {
                tanksUpgrade.removeFirstLiquidFromTank(tank);
                return;
            }
            if (!tanksUpgrade.canFillTank(tank)) return;
            tanksUpgrade.addLiquidToTank(currentItem, tank);
            tanksUpgrade.getInventory().getItem(index).setType(Material.BUCKET);
        }
    }
}
