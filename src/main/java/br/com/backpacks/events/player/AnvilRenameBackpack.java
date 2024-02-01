package br.com.backpacks.events.player;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.recipes.RecipesNamespaces;
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
        if(!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        backPack.setName(event.getCurrentItem().getItemMeta().getDisplayName());
    }
}
