package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.events.upgrades.Jukebox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static br.com.backpacks.events.upgrades.Jukebox.discsSlots;

public class JukeboxUpgrade extends Upgrade {

    private HashMap<Integer, ItemStack> discs = new HashMap<>();
    private ItemStack playing;
    private Boolean isPlaying;
    private Sound sound;

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public JukeboxUpgrade(int id){
        super(UpgradeType.JUKEBOX, id);
        this.isPlaying = false;
        this.inventory = Bukkit.createInventory(null, 27, "Jukebox");
        updateInventory();
    }

    public HashMap<Integer, ItemStack> getDiscs() {
        return discs;
    }

    public ItemStack getPlaying() {
        return playing;
    }

    public void setPlaying(ItemStack playing) {
        this.playing = playing;
    }

    public Boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean playing) {
        isPlaying = playing;
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
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for (int i : Jukebox.blankSlots) {
            inventory.setItem(i, blank);
        }

        for (int i : discsSlots) {
            inventory.setItem(i, getDiscs().get(i));
        }

        inventory.setItem(10, play);
        inventory.setItem(11, stop);
        inventory.setItem(13, getPlaying());
    }
}
