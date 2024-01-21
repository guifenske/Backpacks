package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Jukebox implements Listener {

    protected static Set<Integer> blankSlots = Set.of(0,1,2,3,4,5,9,12,14,18,19,20,21,22,23);

    private static HashMap<Integer, JukeboxUpgrade> currentJukebox = new HashMap<>();

    protected static Set<Integer> discsSlots = Set.of(6,7,8,15,16,17,24,25,26);

    public static Inventory inventory(Player player, BackPack backPack, int jukeboxId){
        Inventory inventory = Bukkit.createInventory(player, 27, "Jukebox");

        ItemStack play = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Play Music").get();
        ItemStack stop = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Stop Music").get();
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").get();

        for (int i : blankSlots) {
            inventory.setItem(i, blank);
        }

        JukeboxUpgrade jukeboxUpgrade = (JukeboxUpgrade) backPack.getUpgradeFromId(jukeboxId);
        currentJukebox.put(backPack.getId(), jukeboxUpgrade);

        int i1 = 0;
        if(jukeboxUpgrade.getDiscs() != null && !jukeboxUpgrade.getDiscs().isEmpty()){
            for (int i : discsSlots) {
                if(i1 >= jukeboxUpgrade.getDiscs().size()) break;
                inventory.setItem(i, jukeboxUpgrade.getDiscs().get(i1));
                i1++;
            }
        }

        inventory.setItem(10, play);
        inventory.setItem(11, stop);
        inventory.setItem(13, jukeboxUpgrade.getPlaying());

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
        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK());
        if(backPack == null){
            event.setCancelled(true);
            return;
        }

        if(blankSlots.contains(event.getRawSlot())) event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()){
            case 10 -> {
                event.setCancelled(true);
                if(!canUse){
                    event.getWhoClicked().sendMessage("§cYou can't use this upgrade because your backpack is not in your back.");
                    return;
                }
                if(currentJukebox.get(backPack.getId()).isPlaying()) return;
                if(!checkDisk(event.getInventory().getItem(13))) return;
                currentJukebox.get(backPack.getId()).setIsPlaying(true);
                currentJukebox.get(backPack.getId()).setSound(getSoundFromItem(event.getInventory().getItem(13)));
                startPlaying(player, currentJukebox.get(backPack.getId()).getSound());
            }
            case 11 -> {
                event.setCancelled(true);
                if(!currentJukebox.get(backPack.getId()).isPlaying()) return;
                currentJukebox.get(backPack.getId()).setIsPlaying(false);
                stopPlaying(player);
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
        else currentJukebox.get(backPack.getId()).setPlaying(event.getInventory().getItem(13));

        currentJukebox.get(backPack.getId()).setDiscs(disks);
        currentJukebox.remove(backPack.getId());
        BackpackAction.removeAction((Player) event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    private final HashMap<UUID, net.kyori.adventure.sound.Sound> playing = new HashMap<>();

    public void startPlaying(Entity entity, Sound sound){                                                                                //apparently this is the max volume
        net.kyori.adventure.sound.Sound sound1 = net.kyori.adventure.sound.Sound.sound(sound, net.kyori.adventure.sound.Sound.Source.MASTER, 2147483647, 1);

        playing.put(entity.getUniqueId(), sound1);
        entity.playSound(sound1, net.kyori.adventure.sound.Sound.Emitter.self());
    }

    public void stopPlaying(Entity entity){
        if(!playing.containsKey(entity.getUniqueId())) return;
        entity.stopSound(playing.get(entity.getUniqueId()));
        playing.remove(entity.getUniqueId());
    }
}
