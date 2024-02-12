package br.com.backpacks.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.inventory.ItemCreator;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static br.com.backpacks.events.upgrades.Jukebox.discsSlots;

public class JukeboxUpgrade extends Upgrade {
    private Sound sound;

    public static ItemStack getDisableLoopItem() {
        return disableLoopItem;
    }

    private static final ItemStack disableLoopItem = new ItemCreator(Material.RED_STAINED_GLASS, "Disable loop").build();

    public boolean isLooping() {
        return isLooping;
    }

    public void setIsLooping(boolean looping) {
        isLooping = looping;
    }

    private boolean isLooping = false;

    private BukkitTask loopingTask;
    private BukkitTask particleTask;

    public void clearLoopingTask(){
        if(loopingTask != null) loopingTask.cancel();
        loopingTask = null;
    }

    public void clearParticleTask(){
        if(particleTask != null) particleTask.cancel();
        particleTask = null;
    }

    public void startLoopingTask(Entity entity){
        loopingTask = new BukkitRunnable() {
            @Override
            public void run() {
                entity.playSound(getSound(), Sound.Emitter.self());
            }
        }.runTaskTimer(Main.getMain(), 0L, (Jukebox.durationFromDisc(inventory.getItem(13)) * 20));
    }

    public void startLoopingTask(Location loc){
        loopingTask = new BukkitRunnable() {
            @Override
            public void run() {
                loc.getWorld().playSound(getSound());
            }
        }.runTaskTimer(Main.getMain(), 0L, (Jukebox.durationFromDisc(inventory.getItem(13)) * 20));
    }

    public void startParticleTask(Location loc){
        particleTask = new BukkitRunnable() {
            @Override
            public void run() {
                loc.getWorld().spawnParticle(Particle.NOTE, loc, 1);
            }
        }.runTaskTimer(Main.getMain(), 3L, 20L);
    }

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public JukeboxUpgrade(int id){
        super(UpgradeType.JUKEBOX, id);
        this.inventory = Bukkit.createInventory(null, 27, "Jukebox");
        updateInventory();
    }

    public HashMap<Integer, ItemStack> getDiscs() {
        HashMap<Integer, ItemStack> hashMap = new HashMap<>();
        for(int i : discsSlots){
            if(inventory.getItem(i) == null) continue;
            hashMap.put(i, inventory.getItem(i));
        }
        return hashMap;
    }

    @Override
    public boolean canReceiveInput(@NotNull ItemStack itemStack) {
        return Jukebox.checkDisk(itemStack);
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }

    @Override
    public List<Integer> inputSlots() {
        return discsSlots;
    }

    @Override
    public List<Integer> outputSlots() {
        return discsSlots;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public ItemStack getSoundFromName(String name) {
        return new ItemStack(Material.getMaterial(name));
    }
    public void updateInventory(){
        ItemStack play = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Play Music").build();
        ItemStack stop = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Stop Music").build();
        ItemStack enableLoop = new ItemCreator(Material.GREEN_STAINED_GLASS, "Enable loop").build();
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for (int i : Jukebox.blankSlots) {
            inventory.setItem(i, blank);
        }

        inventory.setItem(9, enableLoop);
        inventory.setItem(10, play);
        inventory.setItem(11, stop);
    }
}
