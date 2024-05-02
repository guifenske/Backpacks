package br.com.backpacks.events.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.backpacks.BackpackManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class RenameBackpack implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onRename(AsyncChatEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.RENAMING)) return;
        Player player = event.getPlayer();
        TextComponent textComponent = (TextComponent) event.originalMessage();
        String newName = textComponent.content();
        event.setCancelled(true);

        if(newName.isEmpty() || newName.length() > 30){
            player.sendMessage(Main.PREFIX + "§cInvalid name, try again..");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        BackpackAction.clearPlayerAction(player);
        BackPack backPack = BackpackManager.getPlayerCurrentBackpack(player);
        Bukkit.getScheduler().runTask(Main.getMain(), backPack::closeInventoryGlobally);

        if(backPack.getLocation() == null && backPack.getOwner() == null) {
            player.getInventory().remove(backPack.getBackpackItem());
            backPack.setName(newName);
            ItemStack bpItem = RecipesUtils.getItemFromBackpack(backPack);
            player.getInventory().addItem(bpItem);
            player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName + ".");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }

        if(backPack.isShowingNameAbove()) {
            Bukkit.getScheduler().runTask(Main.getMain(), () -> backPack.getMarkerEntity().customName(Component.text(newName)));
        }

        backPack.setName(newName);
        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName + ".");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
