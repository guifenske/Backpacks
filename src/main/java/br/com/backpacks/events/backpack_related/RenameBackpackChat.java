package br.com.backpacks.events.backpack_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RenameBackpackChat implements Listener {

    @EventHandler
    private void onRename(AsyncPlayerChatEvent event){
        if(!Main.backPackManager.isRenaming.containsKey(event.getPlayer().getUniqueId())) return;
        Player player = event.getPlayer();
        String newName = event.getMessage();
        event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isRenaming.get(player.getUniqueId()));

        player.getInventory().remove(Utils.getItemFromBackpack(backPack));
        backPack.setName(newName);
        player.getInventory().addItem(Utils.getItemFromBackpack(backPack));
        Main.backPackManager.isRenaming.remove(player.getUniqueId());

        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
