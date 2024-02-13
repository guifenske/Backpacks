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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class RenameBackpackChat implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onRename(AsyncChatEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.RENAMING)) return;
        Player player = event.getPlayer();
        TextComponent textComponent = (TextComponent) event.originalMessage();
        String newName = textComponent.content();
        event.setCancelled(true);

        BackpackAction.removeAction(player);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
        if(!backPack.isBlock() && backPack.getOwner() == null) {
            player.getInventory().remove(RecipesUtils.getItemFromBackpack(backPack));
            backPack.setName(newName);
            player.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack));
            player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            backPack.getMarker().remove();
            backPack.setMarker(null);
            ArmorStand marker = (ArmorStand) player.getWorld().spawnEntity(backPack.getLocation().clone().add(0, 1, 0).toCenterLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
            marker.setVisible(false);
            marker.setSmall(true);
            marker.customName(Component.text(newName));
            marker.setCustomNameVisible(true);
            marker.setCanTick(false);
            marker.setCanMove(false);
            marker.setCollidable(false);
            marker.setInvulnerable(true);
            marker.setBasePlate(false);
            marker.setMarker(true);
            backPack.setMarker(marker.getUniqueId());
        });

        backPack.setName(newName);
        player.sendMessage(Main.PREFIX + "§aRenamed backpack to " + newName);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
