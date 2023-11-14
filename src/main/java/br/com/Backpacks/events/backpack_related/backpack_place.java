package br.com.Backpacks.events.backpack_related;

import br.com.Backpacks.BackPack;
import br.com.Backpacks.Main;
import br.com.Backpacks.recipes.Recipes;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class backpack_place implements Listener {

    @EventHandler
    private void place_event(BlockPlaceEvent event){
        if(!event.getItemInHand().getType().equals(org.bukkit.Material.CHEST)){
            return;
        }

        if(!event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK())) return;

        ItemMeta meta = event.getItemInHand().getItemMeta();

        int pass_check = 0;

        if(meta.getPersistentDataContainer().has(new Recipes().getNAMESPACE_BACKPACK_ID())){

            if(Main.back.backPackManager.getPlayerBackPacks(event.getPlayer()) != null) {
                for (BackPack backPack : Main.back.backPackManager.getPlayerBackPacks(event.getPlayer())) {
                    if (backPack.getBackpack_id() == meta.getPersistentDataContainer().get(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER)) {
                        event.getPlayer().openInventory(backPack.getCurrent_page());
                        pass_check = 1;
                        break;
                    }
                }
            }   else{
                event.getPlayer().sendMessage(TextColor.color(255, 0, 0) + "Something went wrong, please contact the server admin");
                event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            }
        }   else{
            event.getPlayer().sendMessage(TextColor.color(255, 0, 0) + "Something went wrong, please contact the server admin");
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        }

        if(pass_check == 0){
            event.getPlayer().sendMessage(TextColor.color(255, 0, 0) + "Something went wrong, please contact the server admin");
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        }

        event.setCancelled(true);
    }
}
