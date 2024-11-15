package br.com.backpacks.utils.backpacks;

import br.com.backpacks.Main;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackPackManager {
    private boolean canBeOpen = true;

    private int lastBackpackID = 0;

    private ConcurrentHashMap<Integer, BackPack> backpacks = new ConcurrentHashMap<>();

    private final HashMap<UUID, Integer> currentBackpackId = new HashMap<>();

    private final ConcurrentHashMap<Location, Integer> backpacksPlacedLocations = new ConcurrentHashMap<>();

    private final HashMap<UUID, Integer> currentPage = new HashMap<>();

    public boolean canOpen() {
        return canBeOpen;
    }

    public void setCanBeOpen(boolean canBeOpen) {
        this.canBeOpen = canBeOpen;
    }

    public ConcurrentHashMap<Location, Integer> getBackpacksPlacedLocations() {
        return backpacksPlacedLocations;
    }

    public ConcurrentHashMap<Integer, BackPack> getBackpacks() {
        return backpacks;
    }

    public int getLastBackpackID() {
        return lastBackpackID;
    }

    public void setLastBackpackID(int lastBackpackID) {
        this.lastBackpackID = lastBackpackID;
    }

    public BackPack getBackpackFromLocation(Location location) {
        if(!getBackpacksPlacedLocations().containsKey(location)) return null;
        return getBackpackFromId(backpacksPlacedLocations.get(location));
    }

    public int getPlayerCurrentPage(Player player) {
        return currentPage.get(player.getUniqueId());
    }

    public void setPlayerCurrentPage(Player player, int page){
        currentPage.put(player.getUniqueId(), page);
    }

    public void clearPlayerCurrentPage(Player player){
        currentPage.remove(player.getUniqueId());
    }

    public void setPlayerCurrentBackpack(Player player, BackPack backPack){
        currentBackpackId.put(player.getUniqueId(), backPack.getId());
    }

    public void clearPlayerCurrentBackpack(Player player){
        currentBackpackId.remove(player.getUniqueId());
    }

    public BackPack getPlayerCurrentBackpack(Player player){
        return backpacks.get(currentBackpackId.get(player.getUniqueId()));
    }

    public BackPack getPlayerCurrentBackpack(HumanEntity player){
        return backpacks.get(currentBackpackId.get(player.getUniqueId()));
    }

    public BackPack getBackpackFromId(int id) {
        if(backpacks.containsKey(id)) return backpacks.get(id);
        Main.getMain().getLogger().severe("Backpack with id " + id + " not found!");
        return null;
    }

    public void setBackpacks(ConcurrentHashMap<Integer, BackPack> backpacks) {
        this.backpacks = backpacks;
    }

    public void upgradeBackpack(BackPack backPack) {
        if(backPack == null) return;
        backpacks.remove(backPack.getId());

        BackPack newBackpack = new BackPack(BackpackType.values()[backPack.getType().ordinal() + 1], backPack.getId());
        newBackpack.setName(backPack.getName());

        newBackpack.setUpgradesIds(backPack.getUpgradesIds());
        newBackpack.setInputUpgrade(backPack.getInputUpgrade());
        newBackpack.setOutputUpgrade(backPack.getOutputUpgrade());

        newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());

        if(backPack.getSecondPage() != null){
            newBackpack.getSecondPage().setStorageContents(backPack.getStorageContentsSecondPage());
        }

        newBackpack.setConfigOptionItems();

        this.backpacks.put(newBackpack.getId(), newBackpack);
    }
}

