package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class OnCloseUpgradeMenu implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getActions(event.getPlayer()).contains(BackpackAction.Action.UPGMENU)) return;

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        if(backPack == null) return;

        List<Upgrade> newUpgrades = new ArrayList<>();
        List<Integer> newUpgradesIds = new ArrayList<>();

        for(int i = 0; i < InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i++){
            ItemStack item = event.getInventory().getItem(i);
            if(item == null){
                continue;
            }
            if(RecipesUtils.isItemUpgrade(item)){
                Upgrade upgrade = RecipesUtils.getUpgradeFromItem(item);
                if(!UpgradeManager.canUpgradeStack(upgrade)){
                    boolean shouldSkip = false;
                    for(Upgrade upgrade1 : newUpgrades){
                        if(upgrade1.getType() == upgrade.getType()){
                            event.getInventory().remove(item);
                            event.getPlayer().getInventory().addItem(item);
                            shouldSkip = true;
                            break;
                        }
                    }
                    if(shouldSkip) continue;
                }
                if(item.getAmount() > 1){
                    event.getPlayer().getInventory().addItem(item.subtract());
                    event.getInventory().setItem(i, item.asOne());
                }
                newUpgrades.add(upgrade);
                newUpgradesIds.add(upgrade.getId());
            }   else{
                event.getInventory().remove(item);
                event.getPlayer().getInventory().addItem(item);
            }
        }

        //stop ticking upgrades when not in the backpack
        if(!backPack.getBackpackUpgrade().isEmpty()){
            if(newUpgrades.isEmpty()){
                backPack.stopTickingAllUpgrades();
            }   else{
                for(Upgrade upgrade : backPack.getBackpackUpgrade()){
                    //was removed
                    if(!newUpgradesIds.contains(upgrade.getId())){
                        backPack.stopTickingUpgrade(upgrade.getId());
                    }
                }
            }
        }

        backPack.setBackpackUpgrade(newUpgrades);
        InventoryBuilder.updateConfigInv(backPack);
        InventoryBuilder.updateEditIOInv(backPack);
        BackpackAction.clearPlayerActions(event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }
}
