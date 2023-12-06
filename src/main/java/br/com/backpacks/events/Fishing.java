package br.com.backpacks.events;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Fishing implements Listener {

    @EventHandler
    private void onFishing(PlayerFishEvent event){
       if(event.getCaught() instanceof Item){

           if(!event.getCaught().getType().equals(EntityType.TROPICAL_FISH)) return;
          // if(ThreadLocalRandom.current().nextDouble(0.01, 100.2) <= 0.2){
           event.setExpToDrop(20);
           Item item = (Item) event.getCaught();
           ItemStack wetbackpack = new ItemStack(Material.CHEST);
           ItemMeta meta = wetbackpack.getItemMeta();
           meta.setDisplayName("Wet Backpack");
           meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
           meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_WET_BACKPACK(), PersistentDataType.INTEGER, 1);
           wetbackpack.setItemMeta(meta);
           item.setItemStack(wetbackpack);
          // }
       }
    }
}
