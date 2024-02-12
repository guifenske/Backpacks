package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
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

        //input
        if (Main.backPackManager.getBackpackFromLocation(destinationLocation) != null) {
            BackPack backPack = Main.backPackManager.getBackpackFromLocation(destinationLocation);
            if(backPack.getInputUpgrade() != -1){
                Upgrade upgrade = UpgradeManager.getUpgradeFromId(backPack.getInputUpgrade());
                //special case of furnace upgrade that have 2 ways of input
                if(upgrade instanceof FurnaceUpgrade){
                    if(sideOfInput.equals(BlockFace.DOWN)){
                        ItemStack itemStack = upgrade.tryAddItem(List.of(0), event.getItem().asOne());
                        if(itemStack != null){
                            event.setCancelled(true);
                            return;
                        }

                        if(((FurnaceUpgrade) upgrade).canTick() && !Furnace.shouldTick.contains(upgrade.getId())){
                            Furnace.shouldTick.add(upgrade.getId());
                            Furnace.tick((FurnaceUpgrade) upgrade);
                        }

                        return;
                    }

                    ItemStack itemStack = upgrade.tryAddItem(List.of(1), event.getItem().asOne());
                    if(itemStack != null){
                        event.setCancelled(true);
                    }
                    return;
                }

                if(!upgrade.canReceiveInput(event.getItem().asOne())){
                    event.setCancelled(true);
                    return;
                }

                ItemStack itemStack = upgrade.tryAddItem(upgrade.inputSlots(), event.getItem().asOne());
                if(itemStack != null){
                    event.setCancelled(true);
                }
                return;
            }

            List<ItemStack> list = backPack.tryAddItem(event.getItem().asOne());
            if (!list.isEmpty()) {
                event.setCancelled(true);
            }
        }

        //output
        else if (Main.backPackManager.getBackpackFromLocation(sourceLocation) != null) {
            BackPack backPack = Main.backPackManager.getBackpackFromLocation(sourceLocation);
            event.setCancelled(true);

            if(backPack.getOutputUpgrade() != -1){
                Upgrade upgrade = UpgradeManager.getUpgradeFromId(backPack.getOutputUpgrade());
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
            }
        }
    }
}
