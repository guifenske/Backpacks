package br.com.Backpacks.events;

import br.com.Backpacks.Main;
import br.com.Backpacks.yaml.YamlUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class player_leave_join implements Listener {

    @EventHandler
    private void on_leave_event(PlayerQuitEvent event) throws IOException {
        YamlUtils.save_backpacks_yaml(event.getPlayer());
    }

    @EventHandler
    private void on_join_event(PlayerJoinEvent event) {
        Main.back.backPackManager.setPlayerBackPacks(event.getPlayer(), YamlUtils.load_backpacks_yaml(event.getPlayer()));
    }

}


