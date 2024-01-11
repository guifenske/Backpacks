package br.com.backpacks.events.backpack_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public class BackpackBreak implements Listener {

    @EventHandler
    private void playerBreak(BlockBreakEvent event){
        if(!event.getBlock().getType().equals(Material.CHEST)) return;
        if(Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation()) == null) return;

        event.setDropItems(false);

        BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getBlock().getLocation());
        ItemStack backpack_item = Utils.getItemFromBackpack(backPack);

        Location location = event.getBlock().getLocation();
        Main.backPackManager.getBackpacksPlacedLocations().remove(location);
        Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
        event.getPlayer().getWorld().dropItemNaturally(location, backpack_item);
    }

    @EventHandler
    private void explosionBreak(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if(!block.getType().equals(Material.CHEST)) continue;
            if(Main.backPackManager.getBackpackFromLocation(block.getLocation()) == null) continue;

            event.setCancelled(true);
            block.setType(Material.AIR);

            BackPack backPack = Main.backPackManager.getBackpackFromLocation(block.getLocation());
            ItemStack backpack_item = Utils.getItemFromBackpack(backPack);

            Location location = block.getLocation();
            Main.backPackManager.getBackpacksPlacedLocations().remove(location);
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
            block.getWorld().dropItemNaturally(location, backpack_item);
            break;
       }
    }
}