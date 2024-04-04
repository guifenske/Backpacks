package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.FurnaceUpgradeEvents;
import br.com.backpacks.upgrades.FilterUpgrade;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HopperEvents implements Listener {
    @EventHandler
    private void moveItem(InventoryMoveItemEvent event) {
        Location destinationLocation = event.getDestination().getLocation();
        Location sourceLocation = event.getSource().getLocation();

        if(sourceLocation == null) return;
        if(destinationLocation == null) return;
        BlockFace sideOfInput = sourceLocation.getBlock().getFace(destinationLocation.getBlock());
        if(sideOfInput == null) return;

        //input
        if (Main.backPackManager.getBackpackFromLocation(destinationLocation) != null) {
            BackPack backPack = Main.backPackManager.getBackpackFromLocation(destinationLocation);
            FilterUpgrade advFilterUpgrade = (FilterUpgrade) backPack.getUpgradeFromType(UpgradeType.ADVANCED_FILTER);
            FilterUpgrade filterUpgrade = (FilterUpgrade) backPack.getUpgradeFromType(UpgradeType.FILTER);
            if(advFilterUpgrade != null){
                if(!advFilterUpgrade.getFilteredItems().contains(event.getItem().getType())){
                    if(filterUpgrade == null){
                        event.setCancelled(true);
                        return;
                    }
                    if(!filterUpgrade.getFilteredItems().contains(event.getItem().getType())){
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if(backPack.getInputUpgrade() != -1){
                Upgrade upgrade = UpgradeManager.getUpgradeFromId(backPack.getInputUpgrade());
                if(!backPack.getUpgradesIds().contains(backPack.getInputUpgrade()) || upgrade == null){
                    backPack.setInputUpgrade(-1);
                    event.setCancelled(true);
                    return;
                }
                //special case of furnace upgrade that have 2 ways of input
                if(upgrade instanceof FurnaceUpgrade){
                    if(sideOfInput.equals(BlockFace.DOWN)){
                        ItemStack itemStack = upgrade.tryAddItem(List.of(0), event.getItem().asOne());
                        if(itemStack != null){
                            event.setCancelled(true);
                            return;
                        }

                        if(((FurnaceUpgrade) upgrade).canTick() && !FurnaceUpgradeEvents.shouldTick.contains(upgrade.getId())){
                            FurnaceUpgradeEvents.shouldTick.add(upgrade.getId());
                            FurnaceUpgradeEvents.tick((FurnaceUpgrade) upgrade);
                        }

                        return;
                    }

                    ItemStack itemStack = upgrade.tryAddItem(List.of(1), event.getItem().asOne());
                    if(itemStack != null){
                        event.setCancelled(true);
                    }
                    return;
                }

                if(!upgrade.canReceiveSpecificItemAsInput(event.getItem().asOne())){
                    event.setCancelled(true);
                    return;
                }

                ItemStack itemStack = upgrade.tryAddItem(upgrade.inputSlots(), event.getItem().asOne());
                if(itemStack != null){
                    event.setCancelled(true);
                }
                return;
            }

            ItemStack itemStack = backPack.tryAddItem(event.getItem().asOne());
            backPack.updateBarrelBlock();
            if (itemStack != null) {
                event.setCancelled(true);
                return;
            }
        }

        //output
        else if (Main.backPackManager.getBackpackFromLocation(sourceLocation) != null) {
            BackPack backPack = Main.backPackManager.getBackpackFromLocation(sourceLocation);
            event.setCancelled(true);

            if(backPack.getOutputUpgrade() != -1){
                Upgrade upgrade = UpgradeManager.getUpgradeFromId(backPack.getOutputUpgrade());
                if(!backPack.getUpgradesIds().contains(backPack.getInputUpgrade()) || upgrade == null){
                    backPack.setOutputUpgrade(-1);
                    event.setCancelled(true);
                    return;
                }

                if(upgrade.getFirstOutputItem() == null){
                    return;
                }
                if(event.getDestination().addItem(upgrade.getFirstOutputItem().asOne()).isEmpty()){
                    upgrade.getFirstOutputItem().subtract();
                }
                return;
            }

            if (backPack.getFirstItem() == null){
                return;
            }
            if (event.getDestination().addItem(backPack.getFirstItem().asOne()).isEmpty()) {
                backPack.getFirstItem().subtract();
                backPack.updateBarrelBlock();
            }
        }
    }
}
