package br.com.backpacks.events.player;

import br.com.backpacks.backpackUtils.BackpackAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvent implements Listener {
    @EventHandler
    private void onLeave(PlayerQuitEvent event){
        BackpackAction.removeAction(event.getPlayer());
    }
}
