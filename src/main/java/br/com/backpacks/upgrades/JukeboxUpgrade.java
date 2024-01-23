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

import java.util.ArrayList;
import java.util.List;

import static br.com.backpacks.events.upgrades.Jukebox.discsSlots;

public class JukeboxUpgrade extends Upgrade {

    private List<ItemStack> discs = new ArrayList<>();
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
    }

    public List<ItemStack> getDiscs() {
        return discs;
    }

    public void setDiscs(List<ItemStack> discs) {
        this.discs = discs;
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

    public List<String> serializeDiscs(){
        List<String> list = new ArrayList<>();
            for(ItemStack itemStack : getDiscs()){
                if(itemStack == null){
                    list.add(null);
                    continue;
                }
                list.add(itemStack.getType().name());
            }
        return list;
    }

    public void updateInventory(){
        ItemStack play = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Play Music").get();
        ItemStack stop = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Stop Music").get();
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").get();

        for (int i : Jukebox.blankSlots) {
            inventory.setItem(i, blank);
        }

        int i1 = 0;
        if(getDiscs() != null && !getDiscs().isEmpty()){
            for (int i : discsSlots) {
                if(i1 >= getDiscs().size()) break;
                inventory.setItem(i, getDiscs().get(i1));
                i1++;
            }
        }

        inventory.setItem(10, play);
        inventory.setItem(11, stop);
        inventory.setItem(13, getPlaying());
    }
}
