package br.com.backpacks.events.player;

import br.com.backpacks.Main;
import br.com.backpacks.advancements.BackpacksAdvancements;
import br.com.backpacks.advancements.NamespacesAdvacements;
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
           if(Main.debugMode || ThreadLocalRandom.current().nextInt(1, 90) == 89){
               event.setExpToDrop(20);
               Item item = (Item) event.getCaught();
               ItemStack wetbackpack = new ItemStack(Material.CHEST);
               ItemMeta meta = wetbackpack.getItemMeta();
               meta.setDisplayName("Wet Backpack");
               meta.setLore(Arrays.asList("Uhh, it looks really WET and unusable..", "Humm, what i could do with it?"));
               meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
               meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_WET_BACKPACK(), PersistentDataType.INTEGER, 1);
               wetbackpack.setItemMeta(meta);
               item.setItemStack(wetbackpack);
               BackpacksAdvancements.displayTo(event.getPlayer(), wetbackpack.getType().toString(), "Wow, thats a huge 'fish'", BackpacksAdvancements.Style.TASK, NamespacesAdvacements.getCAUGHT_A_BACKPACK());
           }
       }
    }
}
