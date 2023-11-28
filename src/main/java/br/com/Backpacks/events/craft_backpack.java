package br.com.Backpacks.events;

import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackpackType;
import br.com.Backpacks.recipes.RecipesNamespaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class craft_backpack implements Listener {
    private int generate_id(){
        Main.back.backPackManager.getBackpacks_ids().add(Main.back.backPackManager.getBackpacks_ids().size() + 1);
        return Main.back.backPackManager.getBackpacks_ids().size();
    }

    @EventHandler
    private void craft_backpack_event(CraftItemEvent event){
        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK())){
            int id = generate_id();
            Main.back.backPackManager.createBackPack((Player) event.getWhoClicked(), 18, "Leather Backpack", id, BackpackType.LEATHER);
            update_result(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK())){
            int old_id = -1;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            if(old_id == -1){
                event.getWhoClicked().sendMessage("It looks that you don't have a backpack to upgrade in the recipe!");
                event.setCancelled(true);
                return;
            }

            update_result(event, old_id);

            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.LEATHER, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK())){
            int old_id = -1;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            if(old_id == -1){
                event.getWhoClicked().sendMessage("It looks that you don't have a backpack to upgrade in the recipe!");
                event.setCancelled(true);
                return;
            }

            update_result(event, old_id);
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.IRON, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK())){
            int old_id = -1;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            if(old_id == -1){
                event.getWhoClicked().sendMessage("It looks that you don't have a backpack to upgrade in the recipe!");
                event.setCancelled(true);
                return;
            }

            update_result(event, old_id);
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.GOLD, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK())){
            int old_id = -1;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            if(old_id == -1){
                event.getWhoClicked().sendMessage("It looks that you don't have a backpack to upgrade in the recipe!");
                event.setCancelled(true);
                return;
            }

            update_result(event, old_id);
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.LAPIS, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
            int old_id = -1;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            if(old_id == -1){
                event.getWhoClicked().sendMessage("It looks that you don't have a backpack to upgrade in the recipe!");
                event.setCancelled(true);
                return;
            }

            update_result(event, old_id);
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.AMETHYST, old_id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK())){
            int old_id = -1;
            for(ItemStack itemStack : event.getInventory().getMatrix()){
                if(itemStack == null) continue;

                if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
                    old_id = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
                }
            }

            if(old_id == -1){
                event.getWhoClicked().sendMessage("It looks that you don't have a backpack to upgrade in the recipe!");
                event.setCancelled(true);
                return;
            }

            update_result(event, old_id);
            Main.back.backPackManager.upgrade_backpack((Player) event.getWhoClicked(), BackpackType.DIAMOND, old_id);
        }
    }

    private void update_result(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        event.getInventory().setResult(itemStack);
    }

}
