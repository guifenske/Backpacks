package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.view.FurnaceView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Furnace implements Listener {
    public static final ConcurrentHashMap<UUID, FurnaceUpgrade> currentFurnace = new ConcurrentHashMap<>();

    @EventHandler
    private void onFurnaceExplode(EntityExplodeEvent event){
        for(Upgrade upgrade : UpgradeManager.getUpgrades().values()){
            if(!upgrade.getType().equals(UpgradeType.FURNACE)) continue;
            FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;

            if(furnaceUpgrade.getFurnace() == null) continue;
            for(Block block : event.blockList()){
                if(block.getLocation().equals(furnaceUpgrade.getFurnace().getLocation())){
                    event.blockList().remove(block);
                    return;
                }
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        Player player = (Player) event.getPlayer();

        if(event.getInventory().getViewers().size() == 1){

            FurnaceView view = (FurnaceView) player.getOpenInventory();

            if(view.getBurnTime() == 0.0 && currentFurnace.get(player.getUniqueId()).canBeRemoved()){
                currentFurnace.get(player.getUniqueId()).stopTicking();
                Main.debugMessage("Removing virtual furnace id " + currentFurnace.get(player.getUniqueId()).getId());
            }
        }

        BackpackAction.clearPlayerAction(player);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);

    }
}
