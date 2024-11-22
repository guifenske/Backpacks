package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.persistence.PersistentDataType;

public class OpenEquippedBackpack implements Listener {

    @EventHandler
    private static void onSwap(PlayerSwapHandItemsEvent event){
        if(!event.getPlayer().isSneaking()) return;
        if(!event.getPlayer().getPersistentDataContainer().has(BackpackRecipes.HAS_BACKPACK)) return;

        Player player = event.getPlayer();

        //invalid backpack
        if(!Main.backPackManager.getBackpacks().containsKey(player.getPersistentDataContainer().get(BackpackRecipes.HAS_BACKPACK, PersistentDataType.INTEGER))){
            player.getPersistentDataContainer().remove(BackpackRecipes.HAS_BACKPACK);
            return;
        }

        BackPack backPack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(BackpackRecipes.HAS_BACKPACK, PersistentDataType.INTEGER));
        event.setCancelled(true);

        backPack.open(player);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
    }

}
