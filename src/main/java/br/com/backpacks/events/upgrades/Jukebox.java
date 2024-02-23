package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.others.JukeboxUtils;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Jukebox implements Listener {

    public static List<Integer> blankSlots = List.of(0,1,2,3,4,5,12,14,18,19,20,21,22,23);

    public static List<Integer> discsSlots = List.of(6,7,8,15,16,17,24,25,26);

    public static Boolean checkDisk(@NotNull ItemStack itemStack){
        return durationFromDisc(itemStack) > 0;
    }

    public static net.kyori.adventure.sound.Sound getSoundFromItem(@NotNull ItemStack itemStack){
        org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(itemStack.getType().name());
        return Sound.sound(bukkitSound, Sound.Source.VOICE, 1, 1);
    }

    public static void playSound(JukeboxUpgrade upgrade, Entity entity) {
        if(upgrade.isLooping()){
            upgrade.setOwner(entity);
            upgrade.startLoopingTask(entity);
            return;
        }
        upgrade.setOwner(entity);
        entity.playSound(upgrade.getSound(), Sound.Emitter.self());
    }

    public static void playSound(JukeboxUpgrade upgrade, BackPack backPack) {
        upgrade.startParticleTask(backPack.getLocation().clone().add(0, 1, 0).toBlockLocation().toCenterLocation());
        if(upgrade.isLooping()){
            upgrade.setBackpackId(backPack.getId());
            upgrade.startLoopingTask(backPack.getLocation());
            return;
        }
        upgrade.setBackpackId(backPack.getId());
        backPack.getLocation().getWorld().playSound(upgrade.getSound());
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getActions(event.getWhoClicked()).contains(BackpackAction.Action.UPGJUKEBOX)) return;
        if(blankSlots.contains(event.getRawSlot())){
            event.setCancelled(true);
            return;
        }

        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
        if(backPack.getUpgradesFromType(UpgradeType.JUKEBOX).isEmpty()) return;
        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradesFromType(UpgradeType.JUKEBOX).get(0);
        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER) || backPack.isBlock();

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
                if(backPack.getOwner() == null) playSound(upgrade, backPack);
                else playSound(upgrade, event.getWhoClicked());
            }
            case 11 -> {
                event.setCancelled(true);
                if(!canUse) return;
                if(upgrade.getSound() == null) return;
                if(backPack.getOwner() == null) stopSound(backPack, upgrade);
                else stopSound (event.getWhoClicked(), upgrade);
                upgrade.setSound(null);
            }
        }

    }

    public static void stopSound(BackPack backPack, JukeboxUpgrade upgrade){
        upgrade.clearParticleTask();
        upgrade.clearLoopingTask();
        backPack.getLocation().getWorld().stopSound(upgrade.getSound());
        upgrade.setSound(null);
    }

    public static void stopSound(Entity entity, JukeboxUpgrade upgrade){
        upgrade.clearParticleTask();
        upgrade.clearLoopingTask();
        entity.stopSound(upgrade.getSound());
        upgrade.setSound(null);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getActions(event.getPlayer()).contains(BackpackAction.Action.UPGJUKEBOX)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        JukeboxUpgrade upgrade = (JukeboxUpgrade) backPack.getUpgradesFromType(UpgradeType.JUKEBOX).get(0);
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
        }   else if(event.getInventory().getItem(13) == null && upgrade.getSound() != null){
            if(backPack.getOwner() == null) stopSound(backPack, upgrade);
            else stopSound (event.getPlayer(), upgrade);
        }

        BackpackAction.clearPlayerActions(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open((Player) event.getPlayer());
        }, 1L);
    }


    public static int durationFromDisc(@NotNull ItemStack disc){
        switch (JukeboxUtils.Sound.valueOf(disc.getType().name())){
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
