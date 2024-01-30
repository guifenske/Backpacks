package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Jukebox implements Listener {

    public static Set<Integer> blankSlots = Set.of(0,1,2,3,4,5,9,12,14,18,19,20,21,22,23);

    public static HashMap<UUID, JukeboxUpgrade> currentJukebox = new HashMap<>();

    public static Set<Integer> discsSlots = Set.of(6,7,8,15,16,17,24,25,26);

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
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        if(backPack == null){
            event.setCancelled(true);
            return;
        }

        if(blankSlots.contains(event.getRawSlot())) event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()){
            case 10 -> {
                event.setCancelled(true);
                if(currentJukebox.get(player.getUniqueId()).isPlaying()) return;
                if(!checkDisk(event.getInventory().getItem(13))) return;
                currentJukebox.get(player.getUniqueId()).setIsPlaying(true);
                currentJukebox.get(player.getUniqueId()).setSound(getSoundFromItem(event.getInventory().getItem(13)));
                startPlaying(player, currentJukebox.get(player.getUniqueId()).getSound());
            }
            case 11 -> {
                event.setCancelled(true);
                if(!currentJukebox.get(player.getUniqueId()).isPlaying()) return;
                currentJukebox.get(player.getUniqueId()).setIsPlaying(false);
                stopPlaying(player);
            }
        }
        currentJukebox.get(player.getUniqueId()).updateInventory();
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction(event.getPlayer()) != BackpackAction.Action.UPGJUKEBOX) return;
        UUID uuid = event.getPlayer().getUniqueId();

        for(int i : discsSlots){
            if(event.getInventory().getItem(i) == null){
                currentJukebox.get(uuid).getDiscs().put(i, null);
                continue;
            }
            if(!checkDisk(event.getInventory().getItem(i))){
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(i));
                event.getInventory().remove(event.getInventory().getItem(i));
                currentJukebox.get(uuid).getDiscs().put(i, null);
                continue;
            }

            currentJukebox.get(uuid).getDiscs().put(i, event.getInventory().getItem(i));
        }

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        if(event.getInventory().getItem(13) != null && !checkDisk(event.getInventory().getItem(13)))    event.getPlayer().getInventory().addItem(event.getInventory().getItem(13));
        else currentJukebox.get(uuid).setPlaying(event.getInventory().getItem(13));

        currentJukebox.get(uuid).updateInventory();
        currentJukebox.get(uuid).getViewers().remove((Player) event.getPlayer());
        currentJukebox.remove(uuid);
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
