package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RenameBackpackChat implements Listener {

    @EventHandler
    private void onRename(AsyncPlayerChatEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.RENAMING)) return;
        Player player = event.getPlayer();
        String newName = event.getMessage();
        event.setCancelled(true);

        BackpackAction.removeAction(player);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
        if(!backPack.isBlock() && backPack.getOwner() == null) {
            player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
            backPack.setName(newName);
            player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
            player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }

        backPack.setName(newName);
        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
