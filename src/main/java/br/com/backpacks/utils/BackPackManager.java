package br.com.backpacks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackPackManager {
    public boolean canOpen() {
        return canBeOpen;
    }

    public void setCanBeOpen(boolean canBeOpen) {
        this.canBeOpen = canBeOpen;
    }

    private boolean canBeOpen = true;
    public ConcurrentHashMap<Integer, BackPack> getBackpacks() {
        return backpacks;
    }

    private int backpackIds = 0;

    public int getUpgradesIds() {
        return upgradesIds;
    }

    public void setUpgradesIds(int upgradesIds) {
        this.upgradesIds = upgradesIds;
    }

    private int upgradesIds = 0;
    public int getBackpackIds() {
        return backpackIds;
    }

    public void setBackpackIds(int backpackIds) {
        this.backpackIds = backpackIds;
    }

    public ConcurrentHashMap<Integer, Upgrade> getUpgradeHashMap() {
        return upgradeHashMap;
    }

    private ConcurrentHashMap<Integer, Upgrade> upgradeHashMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer, BackPack> backpacks = new ConcurrentHashMap<>();

    private HashMap<UUID, Integer> currentBackpackId = new HashMap<>();

    public ConcurrentHashMap<Location, Integer> getBackpacksPlacedLocations() {
        return backpacksPlacedLocations;
    }

    public BackPack getBackpackFromLocation(Location location) {
        if(!getBackpacksPlacedLocations().containsKey(location)) return null;
        return getBackpackFromId(backpacksPlacedLocations.get(location));
    }

    private ConcurrentHashMap<Location, Integer> backpacksPlacedLocations = new ConcurrentHashMap<>();
    private HashMap<UUID, Integer> currentPage = new HashMap<>();

    public HashMap<UUID, Integer> getCurrentPage() {
        return currentPage;
    }

    public HashMap<UUID, Integer> getCurrentBackpackId() {
        return currentBackpackId;
    }

    public BackPack getPlayerCurrentBackpack(Player player){
        return backpacks.get(currentBackpackId.get(player.getUniqueId()));
    }

    public BackPack getPlayerCurrentBackpack(HumanEntity player){
        return backpacks.get(currentBackpackId.get(player.getUniqueId()));
    }

    public BackPack getBackpackFromId(int id) {
        if(backpacks.containsKey(id)) return backpacks.get(id);
        throw new NullPointerException("Backpack with id " + id + " not found");
    }

    public BackPack upgradeBackpack(BackpackType oldType, int oldId) {
        if(oldId == -1) return null;

        BackPack oldBackpack = getBackpackFromId(oldId);

        backpacks.remove(oldId);

        switch (oldType){
            case LEATHER ->{
                BackPack backPack = new BackPack("Iron Backpack", Bukkit.createInventory(null, 27, "Iron Backpack"), oldId, BackpackType.IRON);
                backPack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                backPack.setArrowsAndConfigOptionItems();
                backPack.setUpgrades(oldBackpack.getUpgrades());
                backpacks.put(oldId, backPack);
                return backPack;
            }
            case IRON -> {
                BackPack backPack = new BackPack("Gold Backpack", Bukkit.createInventory(null, 36, "Gold Backpack"), oldId, BackpackType.GOLD);
                backPack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                backPack.setArrowsAndConfigOptionItems();
                backPack.setUpgrades(oldBackpack.getUpgrades());
                backpacks.put(oldId, backPack);
                return backPack;
            }
            case GOLD -> {
                BackPack backPack = new BackPack("Lapis Backpack", Bukkit.createInventory(null, 45, "Lapis Backpack"), oldId, BackpackType.LAPIS);
                backPack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                backPack.setArrowsAndConfigOptionItems();
                backPack.setUpgrades(oldBackpack.getUpgrades());
                backpacks.put(oldId, backPack);
                return backPack;
            }
            case LAPIS -> {
                BackPack backPack = new BackPack("Amethyst Backpack", Bukkit.createInventory(null, 54, "Amethyst Backpack"), oldId, BackpackType.AMETHYST);
                backPack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                backPack.setArrowsAndConfigOptionItems();
                backPack.setUpgrades(oldBackpack.getUpgrades());
                backpacks.put(oldId, backPack);
                return backPack;
            }
            case AMETHYST -> {
                BackPack backPack = new BackPack("Diamond Backpack", Bukkit.createInventory(null, 54, "Diamond Backpack"), Bukkit.createInventory(null, 27, "Diamond Backpack"), oldId, BackpackType.DIAMOND);
                backPack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                backPack.setArrowsAndConfigOptionItems();
                backPack.setUpgrades(oldBackpack.getUpgrades());
                backpacks.put(oldId, backPack);
                return backPack;
            }
            case DIAMOND -> {
                BackPack backPack = new BackPack("Netherite Backpack", Bukkit.createInventory(null, 54, "Netherite Backpack"), Bukkit.createInventory(null, 54, "Netherite Backpack"), oldId, BackpackType.NETHERITE);
                backPack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                backPack.getSecondPage().setStorageContents(oldBackpack.getStorageContentsSecondPage());
                backPack.setArrowsAndConfigOptionItems();
                backPack.setUpgrades(oldBackpack.getUpgrades());
                backpacks.put(oldId, backPack);
                return backPack;
            }
        }

        return null;
    }

    public int getSizeFirstPageFromBackpackType(BackpackType type){
        switch (type){
            case LEATHER: return 18;
            case IRON: return 27;
            case GOLD: return 36;
            case LAPIS: return 45;
            case AMETHYST, DIAMOND, NETHERITE: return 54;
        }

        return 0;
    }

    public int getSizeSecondPageFromBackpackType(BackpackType type){
        switch (type){
            case DIAMOND: return 27;
            case NETHERITE: return 54;
        }

        return 0;
    }
}

