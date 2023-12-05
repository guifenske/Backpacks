package br.com.backpacks.events.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!Main.backPackManager.isInBackpackConfig.containsKey(event.getWhoClicked().getUniqueId())) return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        switch (event.getRawSlot()){
            //go back to the previous page
            case 45:
                BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isInBackpackConfig.get(player.getUniqueId()));
                Main.backPackManager.isInBackpackConfig.remove(player.getUniqueId());
                backPack.open(player);
                break;
            //equip or un-equip backpack in the back
            case 53:
                if(event.getClickedInventory().getItem(53) == null) return;

                BackPack backPack1 = Main.backPackManager.getBackpackFromId(Main.backPackManager.isInBackpackConfig.get(player.getUniqueId()));

                if(player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())){
                    player.getInventory().addItem(Utils.getItemFromBackpack(backPack1));
                    player.getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
                }   else{
                    player.getInventory().remove(Utils.getItemFromBackpack(backPack1));
                    player.getPersistentDataContainer().set(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER, Main.backPackManager.isInBackpackConfig.get(player.getUniqueId()));
                }

                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        InventoryBuilder.updateInv(player, event.getClickedInventory());
                    }
                };
                runnable.runTaskLater(Main.getMain(), 1);
                break;
            //rename backpack
            case 52:
                Main.backPackManager.isRenaming.put(player.getUniqueId(), Main.backPackManager.isInBackpackConfig.get(player.getUniqueId()));
                Main.backPackManager.isInBackpackConfig.remove(player.getUniqueId());
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
                break;
        }


    }
}
