package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class RenameBackpack implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onRename(AsyncPlayerChatEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.RENAMING)) return;
        Player player = event.getPlayer();
        String newName = event.getMessage();
        event.setCancelled(true);

        if(newName.isEmpty() || newName.length() > 30){
            player.sendMessage(Main.PREFIX + "§cInvalid name, try again..");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        BackpackAction.clearPlayerAction(player);
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(player);

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(UUID uuid : backPack.getViewersIds()){
                if(Main.backPackManager.getCurrentPage().containsKey(uuid)){
                    Player player1 = Bukkit.getPlayer(uuid);
                    if(player1 == null) continue;
                    BackpackAction.clearPlayerAction(player1);
                    BackpackAction.getSpectators().remove(player1.getUniqueId());
                    player1.closeInventory();
                }
            }
        });

        if(!backPack.isBlock() && backPack.getOwner() == null) {
            backPack.removeBackpackItem(player);
            backPack.setName(newName);

            player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
            player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName + ".");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

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
            return;
        }

        if(backPack.isShowingNameAbove()) {
            Bukkit.getScheduler().runTask(Main.getMain(), () -> {
                backPack.getMarkerEntity().setCustomName(newName);
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
        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName + ".");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
