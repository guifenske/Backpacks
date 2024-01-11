package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JukeboxGrid implements Listener {

    protected static Set<Integer> blankSlots = Set.of(0,1,2,3,4,5,9,12,14,18,19,20,21,22,23);

    protected static Set<Integer> discsSlots = Set.of(6,7,8,15,16,17,24,25,26);

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

        for (int i : blankSlots) {
            inventory.setItem(i, blank);
        }

        if(backPack.getDiscs() != null && !backPack.getDiscs().isEmpty()){
            for (int i = 0; i < backPack.getDiscs().size(); i++) {
                inventory.setItem(i + 6, backPack.getDiscs().get(i));
            }
        }

        inventory.setItem(10, play);
        inventory.setItem(11, stop);
        inventory.setItem(13, backPack.getPlaying());

        return inventory;
    }

    private Boolean checkDisk(ItemStack itemStack){
        if(itemStack == null) return false;
        return itemStack.getType().toString().toUpperCase().contains("MUSIC_DISC");
    }

    public Sound getSoundFromItem(ItemStack itemStack){
        if(itemStack == null) return null;
        if(!checkDisk(itemStack)) return null;
        String name = itemStack.getType().name();
        return Sound.valueOf(name);
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(BackpackAction.getAction(event.getWhoClicked()) != BackpackAction.Action.UPGJUKEBOX) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null){
            event.setCancelled(true);
            return;
        }

        if(blankSlots.contains(event.getRawSlot())) event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()){
            case 10 -> {
                event.setCancelled(true);
                if(backPack.isPlaying()) return;
                if(!checkDisk(event.getInventory().getItem(13))) return;
                backPack.setIsPlaying(true);
                backPack.setSound(getSoundFromItem(event.getInventory().getItem(13)));
                Jukebox.startPlaying(player, backPack.getSound());
            }
            case 11 -> {
                event.setCancelled(true);
                if(!backPack.isPlaying()) return;
                backPack.setIsPlaying(false);
                Jukebox.stopPlaying(player);
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction(event.getPlayer()) != BackpackAction.Action.UPGJUKEBOX) return;

        List<ItemStack> disks = new ArrayList<>();

        for(int i : discsSlots){
            if(event.getInventory().getItem(i) == null){
                disks.add(null);
                continue;
            }
            if(!checkDisk(event.getInventory().getItem(i))){
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(i));
                disks.add(null);
                continue;
            }

            disks.add(event.getInventory().getItem(i));
        }

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        if(event.getInventory().getItem(13) != null && !checkDisk(event.getInventory().getItem(13)))    event.getPlayer().getInventory().addItem(event.getInventory().getItem(13));
        else backPack.setPlaying(event.getInventory().getItem(13));

        backPack.setDiscs(disks);
        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);
        backPack.open((Player) event.getPlayer());
    }
}
