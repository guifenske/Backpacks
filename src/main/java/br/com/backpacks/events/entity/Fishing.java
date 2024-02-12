package br.com.backpacks.events.entity;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Fishing implements Listener {

    @EventHandler
    private void onFishing(PlayerFishEvent event){
       if(event.getCaught() instanceof Item){
           if(ThreadLocalRandom.current().nextInt(1, 100) == 69){
               event.setExpToDrop(20);
               Item item = (Item) event.getCaught();
               ItemStack wetbackpack = new ItemStack(Material.BARREL);
               ItemMeta meta = wetbackpack.getItemMeta();
               meta.setDisplayName("Wet Backpack");
               meta.setLore(Arrays.asList("Uhh, it looks really WET and unusable..", "Humm, what i could do with it?"));
               meta.getPersistentDataContainer().set(new RecipesNamespaces().isBackpack(), PersistentDataType.INTEGER, -1);
               meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_WET_BACKPACK(), PersistentDataType.INTEGER, 1);
               wetbackpack.setItemMeta(meta);
               item.setItemStack(wetbackpack);
           }
       }
    }
}
