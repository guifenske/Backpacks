package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import br.com.backpacks.backpack.BackpackEntity;
import br.com.backpacks.scheduler.TickComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvent implements Listener {
    @EventHandler
    private void onLeave(PlayerQuitEvent event){
        BackpackAction.clearPlayerAction(event.getPlayer());

        for(Backpack backpack : Main.backpackManager.getBackpacks().values()){
            if(backpack.getOwner() == null){
                continue;
            }

            if(backpack.getOwner().equals(event.getPlayer().getUniqueId()) && backpack.getBackpackEntity() != null){
                backpack.getBackpackEntity().clear();
                Main.backpackManager.removeBackpackEntity(event.getPlayer().getUniqueId());

                backpack.setBackpackEntity(null);
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event){
        for(Backpack backpack : Main.backpackManager.getBackpacks().values()){
            if(backpack.getOwner() == null){
                continue;
            }

            if(backpack.getOwner().equals(event.getPlayer().getUniqueId())){
                BackpackEntity backpackEntity = new BackpackEntity(event.getPlayer());
                Main.backpackManager.addBackpackEntity(backpackEntity);

                backpack.setBackpackEntity(backpackEntity);
            }
        }
    }

}
