package br.com.Backpacks.backpackUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class BackPackManager {
    public void setPlayerBackPacks(UUID uuid, List<BackPack> backPacks) {
        listPlayersBackPacks.put(uuid, backPacks);
    }

    public Map<Integer, BackPack> getBackpackById() {
        return backpackById;
    }

    private Map<Integer, BackPack> backpackById = new HashMap<>();

    private Map<UUID, List<BackPack>> listPlayersBackPacks = new HashMap<>();
    public List<Integer> getBackpacks_ids() {
        return backpacks_ids;
    }

    private List<Integer> backpacks_ids = new ArrayList<>();

    public Map<Location, BackPack> getBackpacks_placed_locations() {
        return backpacks_placed_locations;
    }

    public BackPack get_backpack_from_location(Location location) {
        return backpacks_placed_locations.get(location);
    }

    private Map<Location, BackPack> backpacks_placed_locations = new HashMap<>();

    public BackPack createBackPack(Player player, int size, String name, int id, BackpackType type) {
        // Create a new backpack with the specified size and name

        if(size > 54){
            BackPack backPack = new BackPack(player, name, Bukkit.createInventory(null, 54, name), Bukkit.createInventory(null, size - 54, name), id, type);
            listPlayersBackPacks.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(backPack);
            backpackById.put(id, backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(player, name, Bukkit.createInventory(null, size, name), id, type);
        listPlayersBackPacks.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(backPack);
        backpackById.put(id, backPack);
        return backPack;
    }

    public BackPack get_backpack_from_id(int id) {
        return backpackById.get(id);
    }

    public void upgrade_backpack(Player player, BackpackType old_type, int old_id) {
        BackPack old_backpack = get_backpack_from_id(old_id);
        if(old_backpack == null){
            player.sendMessage("§cFailed to upgrade backpack!");
            return;
        }

        Inventory old_first_page = old_backpack.getFirst_page();
        Inventory old_second_page = old_backpack.getSecond_page();

        remove_backpack(old_backpack.getOwner().getUniqueId(), old_backpack);

        switch (old_type){
            case LEATHER:
                BackPack ironBackpack = createBackPack(player, 27, "Iron Backpack", old_id, BackpackType.IRON);
                ironBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                ironBackpack.setOwner(player);
                listPlayersBackPacks.get(player.getUniqueId()).add(ironBackpack);
                break;
            case IRON:
                BackPack goldBackpack = createBackPack(player, 36, "Gold Backpack", old_id, BackpackType.GOLD);
                goldBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                goldBackpack.setOwner(player);
                listPlayersBackPacks.get(player.getUniqueId()).add(goldBackpack);
                break;
            case GOLD:
                BackPack lapisBackpack = createBackPack(player, 45, "Lapis Backpack", old_id, BackpackType.LAPIS);
                lapisBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                lapisBackpack.setOwner(player);
                listPlayersBackPacks.get(player.getUniqueId()).add(lapisBackpack);
                break;
            case LAPIS:
                BackPack amethystBackpack = createBackPack(player, 54, "Amethyst Backpack", old_id, BackpackType.AMETHYST);
                amethystBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                amethystBackpack.setOwner(player);
                listPlayersBackPacks.get(player.getUniqueId()).add(amethystBackpack);
                break;
            case AMETHYST:
                BackPack diamondBackpack = createBackPack(player, 81, "Diamond Backpack", old_id, BackpackType.DIAMOND);
                diamondBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                diamondBackpack.setOwner(player);
                listPlayersBackPacks.get(player.getUniqueId()).add(diamondBackpack);
                break;
            case DIAMOND:
                BackPack netheriteBackpack = createBackPack(player, 108, "Netherite Backpack", old_id, BackpackType.NETHERITE);
                netheriteBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                netheriteBackpack.getSecond_page().setStorageContents(old_second_page.getStorageContents());
                netheriteBackpack.setOwner(player);
                listPlayersBackPacks.get(player.getUniqueId()).add(netheriteBackpack);
                break;
        }

    }

    public void remove_backpack(UUID uuid, BackPack backPack) {
        listPlayersBackPacks.get(uuid).remove(backPack);
    }

    public Map<UUID, List<BackPack>> getListPlayersBackPacks() {
        // Obtenha a lista de mochilas dos jogadores
        return listPlayersBackPacks;
    }

    public List<BackPack> getPlayerBackpacks(UUID uuid){
        return listPlayersBackPacks.get(uuid);
    }
}

