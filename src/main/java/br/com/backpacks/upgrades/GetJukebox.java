package br.com.backpacks.upgrades;

import org.bukkit.inventory.ItemStack;

public interface GetJukebox {

    ItemStack[] getDisks();

    void setDisks(ItemStack[] disks);

    ItemStack getPlaying();

    void setPlaying(ItemStack playing);

    Boolean isPlaying();

    void setIsPlaying(Boolean playing);




}
