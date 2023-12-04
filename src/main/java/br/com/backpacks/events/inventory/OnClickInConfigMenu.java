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
import org.bukkit.scheduler.BukkitRunnable;

public class OnClickInConfigMenu implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!Main.backPackManager.isInBackpackConfig.containsKey((Player) event.getWhoClicked())) return;
        if(!event.getClickedInventory().getType().equals(InventoryType.CHEST)) return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        Main.getMain().getLogger().warning("Clicked in config");

        switch (event.getRawSlot()){
            //go back to the previous page
            case 45:
                BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.isInBackpackConfig.get(player));
                Main.backPackManager.isInBackpackConfig.remove(player);
                backPack.open(player);
                break;
            case 53:
                if(player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())){
                    player.getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
                }   else player.getPersistentDataContainer().set(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER, 1);

                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        InventoryBuilder.updateInv(player, event.getClickedInventory());
                    }
                };
                runnable.runTaskLater(Main.getMain(), 1);
                break;
            case 52:
                Main.backPackManager.isRenaming.put(player, Main.backPackManager.isInBackpackConfig.get(player));
                Main.backPackManager.isInBackpackConfig.remove(player);
                player.sendMessage(Main.PREFIX + "§eType the new name of the backpack");
                player.closeInventory();
                break;
        }


    }
}
