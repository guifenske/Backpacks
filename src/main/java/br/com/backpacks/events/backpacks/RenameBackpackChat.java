package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.recipes.RecipesUtils;
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

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
        if(!backPack.isBlock() && !backPack.isBeingWeared()) {
            player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
            backPack.setName(newName);
            player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
            BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
            player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }

        backPack.setName(newName);
        BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}