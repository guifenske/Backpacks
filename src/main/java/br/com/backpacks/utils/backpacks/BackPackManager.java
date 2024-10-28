package br.com.backpacks.utils.backpacks;

import br.com.backpacks.Main;
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

    private int lastBackpackID = 0;
    public int getLastBackpackID() {
        return lastBackpackID;
    }

    public void setLastBackpackID(int lastBackpackID) {
        this.lastBackpackID = lastBackpackID;
    }

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
        Main.getMain().getLogger().severe("Backpack with id " + id + " not found!");
        return null;
    }

    public void setBackpacks(ConcurrentHashMap<Integer, BackPack> backpacks) {
        this.backpacks = backpacks;
    }

    public void upgradeBackpack(BackPack backPack) {
        if(backPack == null) return;
        backpacks.remove(backPack.getId());

        switch (backPack.getType()){
            case LEATHER ->{
                BackPack newBackpack = new BackPack("Iron Backpack", Bukkit.createInventory(null, 27, "Iron Backpack"), backPack.getId(), BackpackType.IRON);
                newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());
                newBackpack.setBackpackUpgrades(backPack.getBackpackUpgrades());
                backpacks.put(backPack.getId(), newBackpack);
            }

            case IRON -> {
                BackPack newBackpack = new BackPack("Gold Backpack", Bukkit.createInventory(null, 36, "Gold Backpack"), backPack.getId(), BackpackType.GOLD);
                newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());
                newBackpack.setBackpackUpgrades(backPack.getBackpackUpgrades());
                backpacks.put(backPack.getId(), newBackpack);
            }

            case GOLD -> {
                BackPack newBackpack = new BackPack("Lapis Backpack", Bukkit.createInventory(null, 45, "Lapis Backpack"), backPack.getId(), BackpackType.LAPIS);
                newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());
                newBackpack.setBackpackUpgrades(backPack.getBackpackUpgrades());
                backpacks.put(backPack.getId(), newBackpack);
            }

            case LAPIS -> {
                BackPack newBackpack = new BackPack("Amethyst Backpack", Bukkit.createInventory(null, 54, "Amethyst Backpack"), backPack.getId(), BackpackType.AMETHYST);
                newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());
                newBackpack.setBackpackUpgrades(backPack.getBackpackUpgrades());
                backpacks.put(backPack.getId(), newBackpack);
            }

            case AMETHYST -> {
                BackPack newBackpack = new BackPack("Diamond Backpack", Bukkit.createInventory(null, 54, "Diamond Backpack"), Bukkit.createInventory(null, 27, "Diamond Backpack"), backPack.getId(), BackpackType.DIAMOND);
                newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());
                newBackpack.setBackpackUpgrades(backPack.getBackpackUpgrades());
                backpacks.put(backPack.getId(), newBackpack);
            }

            case DIAMOND -> {
                BackPack newBackpack = new BackPack("Netherite Backpack", Bukkit.createInventory(null, 54, "Netherite Backpack"), Bukkit.createInventory(null, 54, "Netherite Backpack"), backPack.getId(), BackpackType.NETHERITE);
                newBackpack.getFirstPage().setStorageContents(backPack.getStorageContentsFirstPage());
                newBackpack.getSecondPage().setStorageContents(backPack.getStorageContentsSecondPage());
                newBackpack.setBackpackUpgrades(backPack.getBackpackUpgrades());
                backpacks.put(backPack.getId(), newBackpack);
            }
        }
    }
}

