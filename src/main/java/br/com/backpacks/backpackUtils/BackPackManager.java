package br.com.backpacks.backpackUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackPackManager {
    public Map<Integer, BackPack> getBackpacks() {
        return backpacks;
    }

    private Map<Integer, BackPack> backpacks = new HashMap<>();

    public ConcurrentHashMap<UUID, Integer> isInBackpack = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Integer> isRenaming = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Integer> isInBackpackConfig = new ConcurrentHashMap<>();

    public Map<Location, BackPack> getBackpacksPlacedLocations() {
        return backpacksPlacedLocations;
    }

    public BackPack getBackpackFromLocation(Location location) {
        return backpacksPlacedLocations.get(location);
    }

    private Map<Location, BackPack> backpacksPlacedLocations = new HashMap<>();

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

    public BackPack getBackpackFromId(int id) {
        return backpacks.get(id);
    }

    public void upgradeBackpack(BackpackType oldType, int oldId) {
        BackPack oldBackpack = getBackpackFromId(oldId);

        Inventory oldFirstPage = oldBackpack.getFirstPage();
        Inventory oldSecondPage = oldBackpack.getSecondPage();

        removeBackpack(oldBackpack);

        switch (oldType){
            case LEATHER:
                BackPack ironBackpack = createBackPack(27, "Iron Backpack", oldId, BackpackType.IRON);
                ironBackpack.getFirstPage().setStorageContents(oldFirstPage.getStorageContents());
                backpacks.remove(oldId);
                backpacks.put(oldId, ironBackpack);
                break;
            case IRON:
                BackPack goldBackpack = createBackPack(36, "Gold Backpack", oldId, BackpackType.GOLD);
                goldBackpack.getFirstPage().setStorageContents(oldFirstPage.getStorageContents());
                backpacks.remove(oldId);
                backpacks.put(oldId, goldBackpack);
                break;
            case GOLD:
                BackPack lapisBackpack = createBackPack(45, "Lapis Backpack", oldId, BackpackType.LAPIS);
                lapisBackpack.getFirstPage().setStorageContents(oldFirstPage.getStorageContents());
                backpacks.remove(oldId);
                backpacks.put(oldId, lapisBackpack);
                break;
            case LAPIS:
                BackPack amethystBackpack = createBackPack(54, "Amethyst Backpack", oldId, BackpackType.AMETHYST);
                amethystBackpack.getFirstPage().setStorageContents(oldFirstPage.getStorageContents());
                backpacks.remove(oldId);
                backpacks.put(oldId, amethystBackpack);
                break;
            case AMETHYST:
                BackPack diamondBackpack = createBackPack(81, "Diamond Backpack", oldId, BackpackType.DIAMOND);
                diamondBackpack.getFirstPage().setStorageContents(oldFirstPage.getStorageContents());
                backpacks.remove(oldId);
                backpacks.put(oldId, diamondBackpack);
                break;
            case DIAMOND:
                BackPack netheriteBackpack = createBackPack(108, "Netherite Backpack", oldId, BackpackType.NETHERITE);
                netheriteBackpack.getFirstPage().setStorageContents(oldFirstPage.getStorageContents());
                netheriteBackpack.getSecondPage().setStorageContents(oldSecondPage.getStorageContents());
                backpacks.remove(oldId);
                backpacks.put(oldId, netheriteBackpack);
                break;
        }
    }

    public void removeBackpack(BackPack backPack) {
        backpacks.remove(backPack.getId());
    }
}

