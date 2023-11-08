package br.com.Backpacks.events;

import br.com.Backpacks.BackPacks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEvent implements Listener {

    @EventHandler
    private void login_event(PlayerJoinEvent event){
        BackPacks backPacks = new BackPacks(event.getPlayer());
        backPacks.create_backpack("opa");

        event.getPlayer().openInventory(backPacks.get_backpack(0));
    }
}
