package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.bukkit.event.block.Action.*;

public class BackpackInteract implements Listener {

    @EventHandler
    private void generalInteractionEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getItem() != null && player.isSneaking()){
            return;
        }

        if(event.getAction().equals(LEFT_CLICK_BLOCK) || event.getAction().equals(LEFT_CLICK_AIR)){
            return;
        }

        if(event.getAction().equals(RIGHT_CLICK_BLOCK) && (event.getClickedBlock().getType().equals(Material.BARREL))){
            if(Main.backPackManager.getBackpackFromLocation(event.getClickedBlock().getLocation()) == null) return;

            event.setCancelled(true);
            if(Main.backPackManager.canOpen()){
                BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getClickedBlock().getLocation());
                backPack.open(player);
                Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
                barrel.open();
                backPack.setIsBlock(true);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
            }
            return;
        }

        if(event.getItem() == null) return;
        if(!event.getItem().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER)){
            if(event.getItem().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.NAMESPACE_WET_BACKPACK, PersistentDataType.INTEGER)){
                event.getPlayer().sendMessage(Main.PREFIX + "§cHumm, this thing is too wet to be used as a backpack.");
                event.setCancelled(true);
            }
            return;
        }

        event.setCancelled(true);
        if(Main.backPackManager.canOpen()) {
            BackPack backPack = Main.backPackManager.getBackpackFromId(event.getItem().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER));
            if(backPack == null) return;
            backPack.setIsBlock(false);

            backPack.open(player);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        }
    }
}