package br.com.backpacks.events.entity;

import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Fishing implements Listener {

    @EventHandler
    private void onFishing(PlayerFishEvent event){
       if(event.getCaught() instanceof Item item){
           if(Constants.CATCH_BACKPACK && ThreadLocalRandom.current().nextInt(1, 200) == 77){
               event.setExpToDrop(20);
               ItemStack wetBackpack = new ItemStack(Material.BARREL);

               ItemMeta meta = wetBackpack.getItemMeta();
               meta.setRarity(ItemRarity.EPIC);

               meta.setDisplayName("Wet Backpack");
               meta.setLore(Arrays.asList("Uhh, it looks really WET and unusable..", "Humm, what could I do with it?"));

               meta.getPersistentDataContainer().set(BackpackRecipes.IS_BACKPACK, PersistentDataType.INTEGER, -1);
               meta.getPersistentDataContainer().set(BackpackRecipes.NAMESPACE_WET_BACKPACK, PersistentDataType.INTEGER, 1);

               wetBackpack.setItemMeta(meta);
               item.setItemStack(wetBackpack);
           }
       }
    }
}
