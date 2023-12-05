package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftBackpack implements Listener {

    private static final String DONTHAVEBACKPACKMSG = "It looks that you don't have a backpack to upgrade in the recipe!";

    private int generateId(){
        return Main.backPackManager.getBackpacks().size();
    }

    @EventHandler
    private void craftBackpackEvent(CraftItemEvent event){

        if(!event.getRecipe().getResult().getType().equals(Material.CHEST)) return;

        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
            Main.getMain().getThreadBackpacks().updateDiscoveredRecipes((Player) event.getWhoClicked());
            return;
        }

        int oldId = -1;

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK())){
            int id = generateId();
            Main.backPackManager.createBackPack(18, "Leather Backpack", id, BackpackType.LEATHER);
            updateResult(event, id);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.LEATHER, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.IRON, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.GOLD, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.LAPIS, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.AMETHYST, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgradeBackpack(BackpackType.DIAMOND, oldId);
        }
    }

    private static int checkBackpackInTheMatrix(CraftItemEvent event, int oldId) {
        for(ItemStack itemStack : event.getInventory().getMatrix()){
            if(itemStack == null) continue;

            if(itemStack.getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
                oldId = itemStack.getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER);
            }
        }

        if(oldId == -1){
            event.getWhoClicked().sendMessage(DONTHAVEBACKPACKMSG);
            event.setCancelled(true);
            return -1;
        }
        return oldId;
    }

    private void updateResult(CraftItemEvent event, int id) {
        ItemStack itemStack = event.getRecipe().getResult();
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        event.getInventory().setResult(itemStack);
    }

}
