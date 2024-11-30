package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.backpack.Backpack;
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
        if (Main.backpackManager.getBackpackFromLocation(destinationLocation) != null) {
            Backpack backpack = Main.backpackManager.getBackpackFromLocation(destinationLocation);
            if(backpack.getInputUpgrade() != -1){
                Upgrade upgrade = UpgradeManager.getUpgradeFromId(backpack.getInputUpgrade());

                if(!backpack.getUpgradesIds().contains(backpack.getInputUpgrade()) || upgrade == null){
                    backpack.setInputUpgrade(-1);
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
            List<ItemStack> list = backpack.tryAddItem(itemStack);

            if (!list.isEmpty()) {
                event.setCancelled(true);
                return;
            }

            backpack.updateBarrelBlock();
        }

        //output
        else if (Main.backpackManager.getBackpackFromLocation(sourceLocation) != null) {
            Backpack backpack = Main.backpackManager.getBackpackFromLocation(sourceLocation);
            event.setCancelled(true);

            if(backpack.getOutputUpgrade() != -1){
                Upgrade upgrade = UpgradeManager.getUpgradeFromId(backpack.getOutputUpgrade());
                if(!backpack.getUpgradesIds().contains(backpack.getInputUpgrade()) || upgrade == null){
                    backpack.setOutputUpgrade(-1);
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

            if (backpack.getFirstItem() == null){
                return;
            }

            ItemStack outputItem = backpack.getFirstItem().clone();
            int amount = outputItem.getAmount();
            outputItem.setAmount(1);

            if (event.getDestination().addItem(outputItem).isEmpty()) {
                backpack.getFirstItem().setAmount(amount - 1);
                backpack.updateBarrelBlock();
            }
        }
    }
}
