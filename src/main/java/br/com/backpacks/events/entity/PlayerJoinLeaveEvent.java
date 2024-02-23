package br.com.backpacks.events.entity;

import br.com.backpacks.utils.BackpackAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvent implements Listener {
    @EventHandler
    private void onLeave(PlayerQuitEvent event){
        BackpackAction.clearPlayerActions(event.getPlayer());
    }
}
