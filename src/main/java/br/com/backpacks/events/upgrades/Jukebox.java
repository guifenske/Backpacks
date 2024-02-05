package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
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

        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK()) && event.getWhoClicked().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER) == backPack.getId();
        if(blankSlots.contains(event.getRawSlot())) event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()){
            case 10 -> {
                event.setCancelled(true);
                if(!canUse){
                    event.getWhoClicked().sendMessage(Main.PREFIX + "§This backpack is not in your back!");
                    return;
                }
                if(currentJukebox.get(player.getUniqueId()).isPlaying()) return;
                if(!checkDisk(event.getInventory().getItem(13))) return;
                currentJukebox.get(player.getUniqueId()).setIsPlaying(true);
                currentJukebox.get(player.getUniqueId()).setSound(getSoundFromItem(event.getInventory().getItem(13)));
                startPlaying(player, currentJukebox.get(player.getUniqueId()).getSound());
            }
            case 11 -> {
                event.setCancelled(true);
                if(!canUse){
                    event.getWhoClicked().sendMessage(Main.PREFIX + "§This backpack is not in your back!");
                    return;
                }
                if(!currentJukebox.get(player.getUniqueId()).isPlaying()) return;
                currentJukebox.get(player.getUniqueId()).setIsPlaying(false);
                stopPlaying(player);
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(BackpackAction.getAction(event.getPlayer()) != BackpackAction.Action.UPGJUKEBOX) return;
        UUID uuid = event.getPlayer().getUniqueId();

        for(int i : discsSlots){
            if(event.getInventory().getItem(i) == null){
                continue;
            }
            if(!checkDisk(event.getInventory().getItem(i))){
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(i));
                event.getInventory().remove(event.getInventory().getItem(i));
            }
        }

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        if(event.getInventory().getItem(13) != null && !checkDisk(event.getInventory().getItem(13)))    event.getPlayer().getInventory().addItem(event.getInventory().getItem(13));
        else currentJukebox.get(uuid).setPlaying(event.getInventory().getItem(13));
        if(event.getInventory().getItem(13) == null && currentJukebox.get(uuid).isPlaying()){
            currentJukebox.get(uuid).setPlaying(null);
            currentJukebox.get(uuid).setIsPlaying(false);
            stopPlaying(event.getPlayer());
        }

        currentJukebox.remove(uuid);
        BackpackAction.removeAction((Player) event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    public void startPlaying(Entity entity, Sound sound){                                                                                //apparently this is the max volume
        net.kyori.adventure.sound.Sound sound1 = net.kyori.adventure.sound.Sound.sound(sound, net.kyori.adventure.sound.Sound.Source.MUSIC, 2147483647, 1);
        entity.playSound(sound1, net.kyori.adventure.sound.Sound.Emitter.self());
    }

    public void stopPlaying(Entity entity){
        int id = entity.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER);
        BackPack backPack = Main.backPackManager.getBackpackFromId(id);
        if(backPack.getUpgradesFromType(UpgradeType.JUKEBOX).isEmpty()) return;

        net.kyori.adventure.sound.Sound sound = net.kyori.adventure.sound.Sound.sound(((JukeboxUpgrade)backPack.getUpgradesFromType(UpgradeType.JUKEBOX).get(0)).getSound(), net.kyori.adventure.sound.Sound.Source.MUSIC, 2147483647, 1);
        entity.stopSound(sound);
    }
}
