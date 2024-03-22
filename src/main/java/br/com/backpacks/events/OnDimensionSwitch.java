package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.UpgradeType;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.persistence.PersistentDataType;

public class OnDimensionSwitch implements Listener {
    @EventHandler
    private void onSwitch(PlayerChangedWorldEvent event){
        if(!event.getPlayer().getPersistentDataContainer().has(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        Player player = event.getPlayer();
        int id = player.getPersistentDataContainer().get(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER);
        BackPack backPack = Main.backPackManager.getBackpackFromId(id);
        if(backPack == null) return;
        if(!backPack.containsUpgradeType(UpgradeType.JUKEBOX)) return;
        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradeFromType(UpgradeType.JUKEBOX);
        if(upgrade.getSound() == null) return;
        Sound sound = upgrade.getSound();
        Jukebox.stopSound(player, upgrade);
        upgrade.setSound(sound);
        if(upgrade.isLooping()){
            upgrade.startLoopingTask(player);
            return;
        }
        Jukebox.playSound(upgrade, player);
    }
}
