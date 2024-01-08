package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class JukeboxGrid implements Listener {

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, 27, "Jukebox");

        ItemStack play = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta playMeta = play.getItemMeta();
        playMeta.setDisplayName("Play Music");
        playMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);
        play.setItemMeta(playMeta);

        ItemStack stop = new ItemStack(Material.BARRIER);
        ItemMeta stopMeta = stop.getItemMeta();
        stopMeta.setDisplayName("Stop Music");
        stopMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 2);
        stop.setItemMeta(stopMeta);

        ItemStack blank = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta blankMeta = blank.getItemMeta();
        blankMeta.setDisplayName(" ");
        blankMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 3);
        blank.setItemMeta(blankMeta);

        Set<Integer> blankSlots = Set.of(0,1,2,3,4,5,9,12,14,18,19,20,21,22,23);
        for (int i : blankSlots) {
            inventory.setItem(i, blank);
        }

        inventory.setItem(10, play);
        inventory.setItem(11, stop);
        inventory.setItem(13, backPack.getPlaying());

        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(BackpackAction.getAction(event.getWhoClicked()) != BackpackAction.Action.UPGJUKEBOX) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null){
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()){
            case 10 -> {
                if(backPack.isPlaying()) return;
                if (backPack.getPlaying() == null) return;

                backPack.setIsPlaying(true);            }
        }


    }
}
