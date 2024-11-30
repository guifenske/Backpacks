package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.upgrades.UpgradeType;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.*;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Jukebox implements Listener {
    public static List<Integer> blankSlots = List.of(0,1,2,3,4,5,12,14,18,19,20,21,22,23);
    public static List<Integer> discsSlots = List.of(6,7,8,15,16,17,24,25,26);

    public static Boolean checkDisk(@NotNull ItemStack itemStack){
        return itemStack.getType().toString().contains("_DISC_");
    }

    public static Sound getSoundFromItem(@NotNull ItemStack itemStack){
        return Sound.sound(Key.key(itemStack.getType().toString().toLowerCase().replace("disc_", "disc.")), Sound.Source.RECORD, 1, 1);
    }

    public static void playSound(JukeboxUpgrade upgrade, Player entity) {
        if(upgrade.isLooping()){
            upgrade.setOwner(entity);
            upgrade.startLoopingTask(entity);
            return;
        }

        upgrade.setOwner(entity);
        entity.playSound(upgrade.getSound(), Sound.Emitter.self());
    }

    public static void playSound(JukeboxUpgrade upgrade, Backpack backpack) {
        upgrade.startParticleTask(backpack.getLocation().toCenterLocation().add(0, 1, 0));

        if(upgrade.isLooping()){
            upgrade.setBackpackId(backpack.getId());
            upgrade.startLoopingTask(backpack.getLocation());
            return;
        }

        upgrade.setBackpackId(backpack.getId());
        backpack.getLocation().getWorld().playSound(backpack.getLocation(), upgrade.getSound().name().value(), SoundCategory.RECORDS, 1, 1);
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGJUKEBOX)) return;

        if(blankSlots.contains(event.getRawSlot())){
            event.setCancelled(true);
            return;
        }

        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        JukeboxUpgrade upgrade = (JukeboxUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);
        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(BackpackRecipes.HAS_BACKPACK) || backpack.isBlock();

        switch (event.getRawSlot()){
            case 9 ->{
                event.setCancelled(true);

                if(!canUse) return;
                if(upgrade.getSound() != null) return;

                if(upgrade.isLooping()){
                    upgrade.setIsLooping(false);
                    upgrade.updateInventory();
                }

                else{
                    upgrade.setIsLooping(true);
                    upgrade.getInventory().setItem(9, JukeboxUpgrade.getDisableLoopItem());
                }
            }

            case 10 -> {
                event.setCancelled(true);
                if(!canUse) return;

                if(event.getInventory().getItem(13) == null) return;
                if(upgrade.getSound() != null) return;
                if(!checkDisk(event.getInventory().getItem(13))) return;

                Sound sound = getSoundFromItem(event.getInventory().getItem(13));
                upgrade.setSound(sound);

                if(backpack.getOwner() == null) playSound(upgrade, backpack);
                else playSound(upgrade, (Player) event.getWhoClicked());
            }

            case 11 -> {
                event.setCancelled(true);
                if(!canUse) return;

                if(upgrade.getSound() == null) return;
                if(backpack.getOwner() == null) stopSound(backpack, upgrade);
                else stopSound ((Player) event.getWhoClicked(), upgrade);
            }

        }

    }

    public static void stopSound(Backpack backpack, JukeboxUpgrade upgrade){
        upgrade.clearParticleTask();
        upgrade.clearLoopingTask();
        backpack.getLocation().getWorld().stopSound(SoundStop.named(upgrade.getSound().name()));
        upgrade.setSound(null);
    }

    public static void stopSound(Player entity, JukeboxUpgrade upgrade){
        upgrade.clearParticleTask();
        upgrade.clearLoopingTask();
        entity.stopSound(upgrade.getSound());
        upgrade.setSound(null);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGJUKEBOX)) return;

        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(event.getPlayer());
        JukeboxUpgrade upgrade = (JukeboxUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);

        for(int i : discsSlots){
            if(event.getInventory().getItem(i) == null) continue;

            if(!checkDisk(event.getInventory().getItem(i))){
                List<ItemStack> itemStack = new ArrayList<>(event.getPlayer().getInventory().addItem(event.getInventory().getItem(i)).values());
                if(!itemStack.isEmpty()){
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack.get(0));
                }

                event.getInventory().setItem(i, null);
            }

        }

        if(event.getInventory().getItem(13) != null && !checkDisk(event.getInventory().getItem(13))){
            List<ItemStack> itemStack = new ArrayList<>(event.getPlayer().getInventory().addItem(event.getInventory().getItem(13)).values());
            if(!itemStack.isEmpty()){
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack.get(0));
            }

            event.getInventory().setItem(13, null);
        }

        else if(event.getInventory().getItem(13) == null && upgrade.getSound() != null){
            if(backpack.getOwner() == null) stopSound(backpack, upgrade);
            else stopSound ((Player) event.getPlayer(), upgrade);
        }

        BackpackAction.clearPlayerAction(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> backpack.open((Player) event.getPlayer()), 1L);
    }


    public static int durationFromDisc(@NotNull ItemStack disc){
        switch (disc.getType()){
            case MUSIC_DISC_13, MUSIC_DISC_5 -> {
                return 178;
            }

            case MUSIC_DISC_CAT, MUSIC_DISC_CHIRP -> {
                return 185;
            }

            case MUSIC_DISC_BLOCKS -> {
                return 345;
            }

            case MUSIC_DISC_FAR -> {
                return 174;
            }

            case MUSIC_DISC_MALL -> {
                return 197;
            }

            case MUSIC_DISC_MELLOHI -> {
                return 96;
            }

            case MUSIC_DISC_STAL -> {
                return 150;
            }

            case MUSIC_DISC_STRAD -> {
                return 188;
            }

            case MUSIC_DISC_WARD -> {
                return 251;
            }

            case MUSIC_DISC_11 -> {
                return 71;
            }

            case MUSIC_DISC_WAIT -> {
                return 238;
            }

            case MUSIC_DISC_OTHERSIDE -> {
                return 195;
            }

            case MUSIC_DISC_PIGSTEP -> {
                return 148;
            }

            case MUSIC_DISC_RELIC -> {
                return 218;
            }
        }

        return 0;
    }
}
