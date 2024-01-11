package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.recipes.RecipesUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class OnCloseUpgradeMenu implements Listener {

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction((Player) event.getPlayer()) != BackpackAction.Action.UPGMENU) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        if(backPack == null) return;

        List<Upgrade> upgrades = new ArrayList<>();

        for(int i = 0; i < InventoryBuilder.getFreeInitialSlots(backPack.getType()); i++){
            if(event.getInventory().getItem(i) == null) continue;
            if(RecipesUtils.getUpgradeFromItem(event.getInventory().getItem(i)) != null){
                upgrades.add(RecipesUtils.getUpgradeFromItem(event.getInventory().getItem(i)));
            }   else event.getPlayer().getInventory().addItem(event.getInventory().getItem(i));
        }
        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);

        backPack.setUpgrades(upgrades);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }
}
