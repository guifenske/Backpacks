package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class Jukebox implements Listener {

    public static Inventory inventory(Player player, BackPack backPack){
        return null;
    }

    private void tick(BackPack backPack, Player player){
        if(player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())){
            if(player.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER) != backPack.getId()){
                return;
            }
        }
    }
}
