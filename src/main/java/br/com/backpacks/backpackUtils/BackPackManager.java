package br.com.backpacks.backpackUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BackPackManager {
    public Map<Integer, BackPack> getBackpacks() {
        return backpacks;
    }

    private Map<Integer, BackPack> backpacks = new HashMap<>();

    public Set<Integer> getBackpacks_ids() {
        return backpacks_ids;
    }

    private Set<Integer> backpacks_ids = new HashSet<>();

    public Map<Location, BackPack> getBackpacks_placed_locations() {
        return backpacks_placed_locations;
    }

    public BackPack get_backpack_from_location(Location location) {
        return backpacks_placed_locations.get(location);
    }

    private Map<Location, BackPack> backpacks_placed_locations = new HashMap<>();

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

    public BackPack get_backpack_from_id(int id) {
        return backpacks.get(id);
    }

    public void upgrade_backpack(BackpackType old_type, int old_id) {
        BackPack old_backpack = get_backpack_from_id(old_id);

        Inventory old_first_page = old_backpack.getFirst_page();
        Inventory old_second_page = old_backpack.getSecond_page();

        remove_backpack(old_backpack);

        switch (old_type){
            case LEATHER:
                BackPack ironBackpack = createBackPack(27, "Iron Backpack", old_id, BackpackType.IRON);
                ironBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                backpacks.remove(old_id);
                backpacks.put(old_id, ironBackpack);
                break;
            case IRON:
                BackPack goldBackpack = createBackPack(36, "Gold Backpack", old_id, BackpackType.GOLD);
                goldBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                backpacks.remove(old_id);
                backpacks.put(old_id, goldBackpack);
                break;
            case GOLD:
                BackPack lapisBackpack = createBackPack(45, "Lapis Backpack", old_id, BackpackType.LAPIS);
                lapisBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                backpacks.remove(old_id);
                backpacks.put(old_id, lapisBackpack);
                break;
            case LAPIS:
                BackPack amethystBackpack = createBackPack(54, "Amethyst Backpack", old_id, BackpackType.AMETHYST);
                amethystBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                backpacks.remove(old_id);
                backpacks.put(old_id, amethystBackpack);
                break;
            case AMETHYST:
                BackPack diamondBackpack = createBackPack(81, "Diamond Backpack", old_id, BackpackType.DIAMOND);
                diamondBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                backpacks.remove(old_id);
                backpacks.put(old_id, diamondBackpack);
                break;
            case DIAMOND:
                BackPack netheriteBackpack = createBackPack(108, "Netherite Backpack", old_id, BackpackType.NETHERITE);
                netheriteBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                netheriteBackpack.getSecond_page().setStorageContents(old_second_page.getStorageContents());
                backpacks.remove(old_id);
                backpacks.put(old_id, netheriteBackpack);
                break;
        }

    }

    public void remove_backpack(BackPack backPack) {
        backpacks.remove(backPack.getBackpack_id());
    }
}

