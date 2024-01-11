package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class BackpackInteract implements Listener {

    @EventHandler
    private void generalInteractionEvent(PlayerInteractEvent event){

        Player player = event.getPlayer();

        if(event.getAction().equals(RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CHEST)){
            if(player.isSneaking()) return;
            if(event.getItem() != null){
                if(event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }

            if(Main.backPackManager.getBackpackFromLocation(event.getClickedBlock().getLocation()) == null) return;

            BackPack backPack = Main.backPackManager.getBackpackFromLocation(event.getClickedBlock().getLocation());
            player.closeInventory();
            event.setCancelled(true);
            backPack.setIsBlock(true);
            backPack.open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
            return;
        }
        if(!event.getAction().equals(RIGHT_CLICK_BLOCK) && !event.getAction().equals(RIGHT_CLICK_AIR)) return;
        if(event.getItem() == null) return;
        if(player.isSneaking() && event.getAction().equals(RIGHT_CLICK_BLOCK)) return;
        if(!event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID())){
            if(event.getItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
                event.getPlayer().sendMessage(Main.PREFIX + "§cHumm, this thing is to wet to be used as a backpack.");
                event.setCancelled(true);
            }
            return;
        }

        BackPack backPack = Main.backPackManager.getBackpackFromId(event.getItem().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        event.setCancelled(true);
        backPack.setIsBlock(false);

        backPack.open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
    }
}