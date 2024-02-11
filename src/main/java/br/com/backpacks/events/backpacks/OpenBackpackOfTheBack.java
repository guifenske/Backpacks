package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.utils.BackPack;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.persistence.PersistentDataType;

public class OpenBackpackOfTheBack implements Listener {

    @EventHandler
    private static void onSwap(PlayerSwapHandItemsEvent event){
        if(!event.getPlayer().isSneaking()) return;
        if(!event.getPlayer().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;
        if(!Main.backPackManager.getBackpacks().containsKey(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER))){
            event.getPlayer().getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
            return;
        }
        BackPack backPack = Main.backPackManager.getBackpackFromId(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        event.setCancelled(true);
        backPack.open(event.getPlayer());
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
    }

}
