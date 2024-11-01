package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
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
                        ItemStack itemStack = upgrade.tryAddItem(List.of(0), event.getItem());
                        if(itemStack != null){
                            event.setCancelled(true);
                            return;
                        }

                        /*
                        if(((FurnaceUpgrade) upgrade).canSmelt() && !Furnace.isTicking.contains(upgrade.getId())){
                            Furnace.isTicking.add(upgrade.getId());
                            Furnace.tick((FurnaceUpgrade) upgrade);
                        }



                         */
                        return;
                    }



                    ItemStack itemStack = upgrade.tryAddItem(List.of(1), event.getItem());
                    if(itemStack != null){
                        event.setCancelled(true);
                    }
                    return;
                }

                if(!upgrade.canReceiveSpecificItemAsInput(event.getItem())){
                    event.setCancelled(true);
                    return;
                }

                ItemStack itemStack = upgrade.tryAddItem(upgrade.inputSlots(), event.getItem());
                if(itemStack != null){
                    event.setCancelled(true);
                }
                return;
            }

            ItemStack itemStack = event.getItem().clone();
            itemStack.setAmount(1);
            List<ItemStack> list = backPack.tryAddItem(itemStack);
            if (!list.isEmpty()) {
                event.setCancelled(true);
                return;
            }
            backPack.updateBarrelBlock();
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

                ItemStack outputItem = upgrade.getFirstOutputItem().clone();
                int amount = outputItem.getAmount();
                outputItem.setAmount(1);

                if(event.getDestination().addItem(outputItem).isEmpty()){
                    upgrade.getFirstOutputItem().setAmount(amount - 1);
                }

                return;
            }

            if (backPack.getFirstItem() == null){
                return;
            }

            ItemStack outputItem = backPack.getFirstItem().clone();
            int amount = outputItem.getAmount();
            outputItem.setAmount(1);

            if (event.getDestination().addItem(outputItem).isEmpty()) {
                backPack.getFirstItem().setAmount(amount - 1);
                backPack.updateBarrelBlock();
            }
        }
    }
}
