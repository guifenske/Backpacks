package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class AutoFeed implements Listener {

    @EventHandler
    private void tick(FoodLevelChangeEvent event){

    }

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, 27, "Auto Feed");
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, "Put your example food in the empty 9x9 space").get();
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();
        Set<Integer> fillSlots = Set.of(3,4,5,12,13,14,21,22,23);

        for (int i = 0; i < 27; i++) {
            if(!fillSlots.contains(i)) inventory.setItem(i, blank);
        }

        if(backPack.isAutoFeedEnabled())    inventory.setItem(10, disable);
        else    inventory.setItem(10, enable);

        int i1 = 0;
        for(int i : fillSlots){
            if(backPack.getAutoFeedItems() != null && !backPack.getAutoFeedItems().isEmpty()) inventory.setItem(i, backPack.getAutoFeedItems().get(i1));
            i1++;
        }

        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGAUTOFEED)) return;
        event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;

        if(event.getRawSlot() == 10){
            if(backPack.isAutoFeedEnabled()){
                backPack.setAutoFeedEnabled(false);
                event.getClickedInventory().setItem(10, new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get());
            }   else{
                backPack.setAutoFeedEnabled(true);
                event.getClickedInventory().setItem(10, new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get());
            }
        }
    }
}
