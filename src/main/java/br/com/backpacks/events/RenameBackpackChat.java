package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RenameBackpackChat implements Listener {

    @EventHandler
    private void onRename(AsyncPlayerChatEvent event){
        if(!Main.backPackManager.isRenaming.containsKey(event.getPlayer())) return;
        Player player = event.getPlayer();
        String newName = event.getMessage();
        event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isRenaming.get(player));

        backPack.setName(newName);

        Main.backPackManager.isRenaming.remove(player);

        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
