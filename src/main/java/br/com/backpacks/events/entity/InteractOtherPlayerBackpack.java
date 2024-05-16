package br.com.backpacks.events.entity;

import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class InteractOtherPlayerBackpack implements Listener {

    @EventHandler
    private void onInteract(PlayerInteractAtEntityEvent event) {
        Entity clickedEntity = event.getRightClicked();
        Player player = event.getPlayer();

        if (!clickedEntity.getPersistentDataContainer().has(BackpackRecipes.getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;

        if(!clickedEntity.getFacing().getDirection().equals(player.getFacing().getDirection()))  return;

        BackPack backPack = BackpackManager.getBackpackFromId(clickedEntity.getPersistentDataContainer().get(BackpackRecipes.getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        if(!backPack.isLocked()){
            backPack.open(player);
            player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_CHEST_OPEN, Sound.Source.BLOCK, 1, 1));
        }
    }
}
