package br.com.backpacks.events.entity;

import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataType;

public class AnvilRenameBackpack implements Listener {

    @EventHandler
    private void playerRenameItemEvent(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!event.getClickedInventory().getType().equals(InventoryType.ANVIL)) return;
        if(!event.getSlotType().equals(InventoryType.SlotType.RESULT)) return;
        if(event.getCurrentItem() == null) return;
        if(!event.getCurrentItem().hasItemMeta()) return;
        if(!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(BackpackRecipes.isBackpack(), PersistentDataType.INTEGER)) return;
        BackPack backPack = BackpackManager.getBackpackFromId(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        backPack.setName(event.getCurrentItem().getItemMeta().getDisplayName());
    }
}
