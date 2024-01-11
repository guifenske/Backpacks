package br.com.backpacks.events.player;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Sound;
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

        if (!clickedEntity.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;

        if(!clickedEntity.getFacing().getDirection().equals(player.getFacing().getDirection()))  return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(clickedEntity.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        if(!backPack.isBlock()){
            backPack.open(player);
            player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1, 1);
        }
    }
}
