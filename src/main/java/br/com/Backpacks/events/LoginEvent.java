package br.com.Backpacks.events;

import br.com.Backpacks.BackPack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEvent implements Listener {

    @EventHandler
    private void login_event(PlayerJoinEvent event){
        if(new BackPack().getBackpacks().get().containsKey(event.getPlayer())) return;
        BackPack backPack = new BackPack(event.getPlayer());
        backPack.create_backpack(54, "opa");
        event.getPlayer().openInventory(backPack.getInventory());
    }
}
