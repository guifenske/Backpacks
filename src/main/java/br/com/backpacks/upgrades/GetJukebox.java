package br.com.backpacks.upgrades;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface GetJukebox {

    List<ItemStack> getDiscs();

    void setDiscs(List<ItemStack> discs);

    ItemStack getPlaying();

    void setPlaying(ItemStack playing);

    Boolean isPlaying();

    void setIsPlaying(Boolean playing);

    Sound getSound();

    void setSound(Sound sound);
    ItemStack getSoundFromName(String name);
}
