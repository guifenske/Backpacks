package br.com.Backpacks.events;

import br.com.Backpacks.Main;
import br.com.Backpacks.recipes.Recipes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class craft_backpack implements Listener {


    private int generate_id(Player player){
        if(!Main.back.backPackManager.getBackpacks_ids().containsKey(player)){
            Main.back.backPackManager.getBackpacks_ids().put(player, new ArrayList<>());
            Main.back.backPackManager.getBackpacks_ids().get(player).add(0);
            return 0;
        }

        Main.back.backPackManager.getBackpacks_ids().get(player).add(Main.back.backPackManager.getBackpacks_ids().get(player).size());
        return Main.back.backPackManager.getBackpacks_ids().get(player).size() - 1;
    }

    @EventHandler
    private void craft_backpack_event(CraftItemEvent event){
        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getIS_BACKPACK())){
            event.setCancelled(true);
            return;
        }
        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LEATHER_BACKPACK())){
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 18, "Leather Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_IRON_BACKPACK())){
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LEATHER_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 27, "Iron Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_GOLD_BACKPACK())){
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_IRON_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 36, "Gold Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LAPIS_BACKPACK())){
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_GOLD_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 45, "Lapis Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_AMETHYST_BACKPACK())){
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_LAPIS_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 54, "Amethyst Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_DIAMOND_BACKPACK())){
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_AMETHYST_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 81, "Diamond Backpack", id);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_NETHERITE_BACKPACK())){
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;
                if(itemStack.getType().equals(org.bukkit.Material.CHEST) && !itemStack.getItemMeta().getPersistentDataContainer().has(new Recipes().getNAMESPACE_DIAMOND_BACKPACK())){
                    event.setCancelled(true);
                    return;
                }
            }
            int id = generate_id(((Player) event.getWhoClicked()));
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 108, "Netherite Backpack", id);
            update_result(event, id);
        }
    }

    private void update_result(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();

        meta.getPersistentDataContainer().set(new Recipes().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        event.getInventory().setResult(itemStack);
    }

}
