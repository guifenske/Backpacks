package br.com.backpacks.events;

import br.com.backpacks.Main;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RenameBackpackChat implements Listener {

    @EventHandler
    private void onRename(AsyncChatEvent event){
        if(!Main.backPackManager.isRenaming.containsKey(event.getPlayer())) return;
        event.setCancelled(true);
        String newName = event.message().toString();
        Main.backPackManager.getBackpackFromId(Main.backPackManager.isRenaming.get(event.getPlayer())).setName(newName);
        Main.backPackManager.isRenaming.remove(event.getPlayer());

        Main.backPackManager.getBackpackFromId(Main.backPackManager.isRenaming.get(event.getPlayer())).open(event.getPlayer());
    }
}
