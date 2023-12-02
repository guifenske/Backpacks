package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftBackpack implements Listener {

    private static final String DONTHAVEBACKPACKMSG = "It looks that you don't have a backpack to upgrade in the recipe!";

    private int generateId(){
        Main.backPackManager.getBackpacks_ids().add(Main.backPackManager.getBackpacks_ids().size() + 1);
        return Main.backPackManager.getBackpacks_ids().size();
    }

    @EventHandler
    private void craftBackpackEvent(CraftItemEvent event){
        if(!event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())) return;

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
            Main.backPackManager.upgrade_backpack(BackpackType.LEATHER, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgrade_backpack(BackpackType.IRON, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgrade_backpack(BackpackType.GOLD, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgrade_backpack(BackpackType.LAPIS, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgrade_backpack(BackpackType.AMETHYST, oldId);
            return;
        }

        if(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK())){
            checkBackpackInTheMatrix(event, oldId);
            if(checkBackpackInTheMatrix(event, oldId) == -1) return;
            updateResult(event, oldId);
            Main.backPackManager.upgrade_backpack(BackpackType.DIAMOND, oldId);
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
