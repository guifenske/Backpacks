package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataType;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!Main.backPackManager.isInBackpackConfig.containsKey((Player) event.getWhoClicked())) return;
        if(!event.getClickedInventory().getType().equals(InventoryType.CHEST)) return;

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()){
            //go back to the previous page
            case 45:
                event.setCancelled(true);
                BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isInBackpackConfig.get(player));
                Main.backPackManager.isInBackpackConfig.remove(player);
                backPack.open(player);
                break;
            case 53:
                event.setCancelled(true);
                if(player.getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())){
                    player.getPersistentDataContainer().remove(new RecipesNamespaces().getIS_BACKPACK());
                }   else player.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);

                InventoryBuilder.updateInv(player);
                break;
            case 52:
                event.setCancelled(true);
                Main.backPackManager.isRenaming.put(player, Main.backPackManager.isInBackpackConfig.get(player));
                Main.backPackManager.isInBackpackConfig.remove(player);
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
                break;
        }


    }
}
