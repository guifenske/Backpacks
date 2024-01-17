package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JukeboxUpgrade extends Upgrade {

    private List<ItemStack> discs = new ArrayList<>();
    private ItemStack playing;
    private Boolean isPlaying;
    private Sound sound;

    public JukeboxUpgrade(int id){
        super(UpgradeType.JUKEBOX, id);
        this.isPlaying = false;
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
}
