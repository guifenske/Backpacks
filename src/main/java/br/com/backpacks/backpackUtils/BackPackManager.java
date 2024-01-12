package br.com.backpacks.backpackUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackPackManager {
    public ConcurrentHashMap<Integer, BackPack> getBackpacks() {
        return backpacks;
    }

    private ConcurrentHashMap<Integer, BackPack> backpacks = new ConcurrentHashMap<>();

    private HashMap<UUID, Integer> currentBackpackId = new HashMap<>();

    private HashMap<UUID, Upgrade> currentUpgradeMenu = new HashMap<>();

    public HashMap<UUID, Upgrade> getCurrentUpgradeMenu() {
        return currentUpgradeMenu;
    }

    public ConcurrentHashMap<Location, Integer> getBackpacksPlacedLocations() {
        return backpacksPlacedLocations;
    }

    public BackPack getBackpackFromLocation(Location location) {
        if(!getBackpacksPlacedLocations().containsKey(location)) return null;
        return getBackpackFromId(backpacksPlacedLocations.get(location));
    }

    private ConcurrentHashMap<Location, Integer> backpacksPlacedLocations = new ConcurrentHashMap<>();

    public BackPack createBackPack(int size, String name, int id, BackpackType type) {
        // Create a new backpack with the specified size and name

        if(size > 54){
            BackPack backPack = new BackPack(name, Bukkit.createInventory(null, 54, name), Bukkit.createInventory(null, size - 54, name), id, type);
            backpacks.put(id, backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, Bukkit.createInventory(null, size, name), id, type);
        backpacks.put(id, backPack);
        return backPack;
    }

    private HashMap<UUID, Integer> currentPage = new HashMap<>();

    public HashMap<UUID, Integer> getCurrentPage() {
        return currentPage;
    }

    public HashMap<UUID, Integer> getCurrentBackpackId() {
        return currentBackpackId;
    }

    public BackPack getBackpackFromId(int id) {
        if(backpacks.containsKey(id)) return backpacks.get(id);
        throw new NullPointerException("Backpack with id " + id + " not found");
    }

    public void upgradeBackpack(BackpackType oldType, int oldId) {
        if(oldId == -1) return;

        BackPack oldBackpack = getBackpackFromId(oldId);

        backpacks.remove(oldId);

        switch (oldType){
            case LEATHER:
                BackPack ironBackpack = new BackPack("Iron Backpack", Bukkit.createInventory(null, 27, "Iron Backpack"), oldId, BackpackType.IRON);
                ironBackpack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                ironBackpack.setArrowsAndConfigOptionItems();
                backpacks.put(oldId, ironBackpack);
                break;
            case IRON:
                BackPack goldBackpack = new BackPack("Gold Backpack", Bukkit.createInventory(null, 36, "Gold Backpack"), oldId, BackpackType.GOLD);
                goldBackpack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                goldBackpack.setArrowsAndConfigOptionItems();
                backpacks.put(oldId, goldBackpack);
                break;
            case GOLD:
                BackPack lapisBackpack = new BackPack("Lapis Backpack", Bukkit.createInventory(null, 45, "Lapis Backpack"), oldId, BackpackType.LAPIS);
                lapisBackpack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                lapisBackpack.setArrowsAndConfigOptionItems();
                backpacks.put(oldId, lapisBackpack);
                break;
            case LAPIS:
                BackPack amethystBackpack = new BackPack("Amethyst Backpack", Bukkit.createInventory(null, 54, "Amethyst Backpack"), oldId, BackpackType.AMETHYST);
                amethystBackpack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                amethystBackpack.setArrowsAndConfigOptionItems();
                backpacks.put(oldId, amethystBackpack);
                break;
            case AMETHYST:
                BackPack diamondBackpack = new BackPack("Diamond Backpack", Bukkit.createInventory(null, 54, "Diamond Backpack"), Bukkit.createInventory(null, 27, "Diamond Backpack"), oldId, BackpackType.DIAMOND);
                diamondBackpack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                diamondBackpack.setArrowsAndConfigOptionItems();
                backpacks.put(oldId, diamondBackpack);
                break;
            case DIAMOND:
                BackPack netheriteBackpack = new BackPack("Netherite Backpack", Bukkit.createInventory(null, 54, "Netherite Backpack"),  Bukkit.createInventory(null, 54, "Netherite Backpack"), oldId, BackpackType.NETHERITE);
                netheriteBackpack.getFirstPage().setStorageContents(oldBackpack.getStorageContentsFirstPage());
                netheriteBackpack.getSecondPage().setStorageContents(oldBackpack.getStorageContentsSecondPage());
                netheriteBackpack.setArrowsAndConfigOptionItems();
                backpacks.put(oldId, netheriteBackpack);
                break;
        }
    }

    public int getSizeFirstPageFromBackpackType(BackpackType type){
        switch (type){
            case LEATHER: return 18;
            case IRON: return 27;
            case GOLD: return 36;
            case LAPIS: return 45;
            case AMETHYST: return 54;
            case DIAMOND: return 54;
            case NETHERITE: return 54;
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

