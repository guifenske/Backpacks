package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class RenameBackpackChat implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onRename(AsyncChatEvent event){
        if(!BackpackAction.getActions(event.getPlayer()).contains(BackpackAction.Action.RENAMING)) return;
        Player player = event.getPlayer();
        TextComponent textComponent = (TextComponent) event.originalMessage();
        String newName = textComponent.content();
        event.setCancelled(true);

        BackpackAction.clearPlayerActions(player);
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(player);

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(UUID uuid : backPack.getViewersIds()){
                if(Main.backPackManager.getCurrentPage().containsKey(uuid)){
                    Player player1 = Bukkit.getPlayer(uuid);
                    if(player1 == null) continue;
                    BackpackAction.clearPlayerActions(player1);
                    player1.closeInventory();
                }
            }
        });

        if(!backPack.isBlock() && backPack.getOwner() == null) {
            player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
            backPack.setName(newName);
            Bukkit.getScheduler().runTask(Main.getMain(), ()->{
                for(UUID uuid : backPack.getViewersIds()){
                    if(Main.backPackManager.getCurrentPage().containsKey(uuid)){
                        Player player1 = Bukkit.getPlayer(uuid);
                        if(player1 == null) continue;
                        if(Main.backPackManager.getCurrentPage().get(uuid) == 1) backPack.open(player1);
                        else backPack.openSecondPage(player1);
                    }
                }
            });
            player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
            player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }

        if(backPack.isShowingNameAbove()) {
            Bukkit.getScheduler().runTask(Main.getMain(), () -> {
                backPack.getMarkerEntity().customName(Component.text(newName));
            });
        }

        backPack.setName(newName);
        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(UUID uuid : backPack.getViewersIds()){
                if(Main.backPackManager.getCurrentPage().containsKey(uuid)){
                    Player player1 = Bukkit.getPlayer(uuid);
                    if(player1 == null) continue;
                    if(Main.backPackManager.getCurrentPage().get(uuid) == 1) backPack.open(player1);
                    else backPack.openSecondPage(player1);
                }
            }
        });
        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
