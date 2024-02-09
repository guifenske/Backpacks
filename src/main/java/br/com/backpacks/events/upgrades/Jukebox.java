package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Jukebox implements Listener {

    public static List<Integer> blankSlots = List.of(0,1,2,3,4,5,9,12,14,18,19,20,21,22,23);

    public static HashMap<UUID, JukeboxUpgrade> currentJukebox = new HashMap<>();

    public static List<Integer> discsSlots = List.of(6,7,8,15,16,17,24,25,26);

    public static Boolean checkDisk(@NotNull ItemStack itemStack){
        return itemStack.getType().name().contains("DISC");
    }

    public net.kyori.adventure.sound.Sound getSoundFromItem(@NotNull ItemStack itemStack){
        org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(itemStack.getType().name());
        Sound sound = Sound.sound(bukkitSound, Sound.Source.RECORD, 1, 1);
        Main.getMain().debugMessage(sound.toString());
        return sound;
    }

    public static void playSound(net.kyori.adventure.sound.Sound sound, Player player) {
        player.playSound(sound, net.kyori.adventure.sound.Sound.Emitter.self());
    }

    public static void playSound(net.kyori.adventure.sound.Sound sound, BackPack backPack) {
        backPack.getLocation().getWorld().playSound(sound);
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGJUKEBOX)) return;
        if(blankSlots.contains(event.getRawSlot())){
            event.setCancelled(true);
            return;
        }

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        if(backPack.getUpgradesFromType(UpgradeType.JUKEBOX).isEmpty()) return;
        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradesFromType(UpgradeType.JUKEBOX).get(0);
        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK()) || backPack.isBlock();

        switch (event.getRawSlot()){
            case 10 -> {
                event.setCancelled(true);
                if(!canUse) return;
                if(event.getInventory().getItem(13) == null) return;
                if(!checkDisk(event.getInventory().getItem(13))) return;
                Sound sound = getSoundFromItem(event.getInventory().getItem(13));
                if(backPack.getOwner() == null) playSound(sound, backPack);
                else playSound(sound, (Player) event.getWhoClicked());
                upgrade.setSound(sound);
            }
            case 11 -> {
                event.setCancelled(true);
                if(!canUse) return;
                if(upgrade.getSound() == null) return;
                if(backPack.getOwner() == null) stopSound(backPack, upgrade);
                else stopSound((Player) event.getWhoClicked(), upgrade);
                upgrade.setSound(null);
            }
        }
    }

    public static void stopSound(BackPack backPack, JukeboxUpgrade upgrade){
        backPack.getLocation().getWorld().stopSound(upgrade.getSound());
    }

    public static void stopSound(Player player, JukeboxUpgrade upgrade){
        player.stopSound(upgrade.getSound());
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGJUKEBOX)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open((Player) event.getPlayer());
        }, 1L);
    }


}
