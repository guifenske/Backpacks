package br.com.backpacks.backpack;

import br.com.backpacks.Main;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackpackManager {
    private boolean canBeOpen = true;

    private int lastBackpackID = 0;

    private ConcurrentHashMap<Integer, Backpack> backpacks = new ConcurrentHashMap<>();

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

    public ConcurrentHashMap<Integer, Backpack> getBackpacks() {
        return backpacks;
    }

    public int getLastBackpackID() {
        return lastBackpackID;
    }

    public void setLastBackpackID(int lastBackpackID) {
        this.lastBackpackID = lastBackpackID;
    }

    public Backpack getBackpackFromLocation(Location location) {
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

    public void setPlayerCurrentBackpack(Player player, Backpack backpack){
        currentBackpackId.put(player.getUniqueId(), backpack.getId());
    }

    public void clearPlayerCurrentBackpack(Player player){
        currentBackpackId.remove(player.getUniqueId());
    }

    public Backpack getPlayerCurrentBackpack(Player player){
        return backpacks.get(currentBackpackId.get(player.getUniqueId()));
    }

    public Backpack getPlayerCurrentBackpack(HumanEntity player){
        return backpacks.get(currentBackpackId.get(player.getUniqueId()));
    }

    public Backpack getBackpackFromId(int id) {
        if(backpacks.containsKey(id)) return backpacks.get(id);
        Main.getMain().getLogger().severe("Backpack with id " + id + " not found!");
        return null;
    }

    public void setBackpacks(ConcurrentHashMap<Integer, Backpack> backpacks) {
        this.backpacks = backpacks;
    }

    public void upgradeBackpack(Backpack backpack) {
        if(backpack == null) return;
        backpacks.remove(backpack.getId());

        Backpack newBackpack = new Backpack(BackpackType.values()[backpack.getType().ordinal() + 1], backpack.getId());
        newBackpack.setName(backpack.getName());

        newBackpack.setUpgradesIds(backpack.getUpgradesIds());
        newBackpack.setInputUpgrade(backpack.getInputUpgrade());
        newBackpack.setOutputUpgrade(backpack.getOutputUpgrade());

        newBackpack.getFirstPage().setStorageContents(backpack.getStorageContentsFirstPage());

        if(backpack.getSecondPage() != null){
            newBackpack.getSecondPage().setStorageContents(backpack.getStorageContentsSecondPage());
        }

        newBackpack.setConfigItems();

        this.backpacks.put(newBackpack.getId(), newBackpack);
    }
}

