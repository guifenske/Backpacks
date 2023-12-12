package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class InteractOtherPlayerBackpack implements Listener {

    @EventHandler
    private void onInteract(PlayerInteractAtEntityEvent event) {
        Player clickedPlayer = (Player) event.getRightClicked();
        Player interactingPlayer = event.getPlayer();

        if (!clickedPlayer.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK()))     return;

        if(!clickedPlayer.getFacing().getDirection().equals(interactingPlayer.getFacing().getDirection()))  return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(clickedPlayer.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack == null) return;

        backPack.open(interactingPlayer);
        interactingPlayer.playSound(interactingPlayer, Sound.BLOCK_CHEST_OPEN, 1, 1);
    }
}
