package br.com.Backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackPackManager {
    public void setPlayerBackPacks(Player player, List<BackPack> backPacks) {
        playerBackPacks.put(player, backPacks);
    }

    private Map<Player, List<BackPack>> playerBackPacks = new HashMap<>();

    private Map<Player, BackPack> current_backpack = new HashMap<>();

    public Map<Player, List<Integer>> getBackpacks_ids() {
        return backpacks_ids;
    }

    private Map<Player, List<Integer>> backpacks_ids = new HashMap<>();

    public BackPack createBackPack(Player player, int size, String name, int id) {
        // Create a new backpack with the specified size and name

        if(size > 54){
            BackPack backPack = new BackPack(name, Bukkit.createInventory(player, 54, name), Bukkit.createInventory(player, size - 54, name), id);
            current_backpack.put(player, backPack);
            playerBackPacks.computeIfAbsent(player, k -> new ArrayList<>()).add(backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, Bukkit.createInventory(player, size, name), id);
        current_backpack.put(player, backPack);
        playerBackPacks.computeIfAbsent(player, k -> new ArrayList<>()).add(backPack);
        return backPack;
    }

    public BackPack get_backpack_from_id(Player player, int id) {
        for(BackPack backPack : playerBackPacks.get(player)){
            if(backPack.getBackpack_id() == id){
                return backPack;
            }
        }
        return null;
    }

    public void upgrade_backpack(Player player, BackpackType old_type, int old_id) {
        Inventory old_first_page = get_backpack_from_id(player, old_id).getFirst_page();
        Inventory old_second_page = get_backpack_from_id(player, old_id).getSecond_page();
        Inventory old_current_page = get_backpack_from_id(player, old_id).getCurrent_page();

        remove_backpack(player, get_backpack_from_id(player, old_id));

        switch (old_type){
            case LEATHER:
                BackPack ironBackpack = createBackPack(player, 27, "Iron Backpack", old_id);
                ironBackpack.setFirst_page(old_first_page);
                ironBackpack.setSecond_page(old_second_page);
                ironBackpack.setCurrent_page(old_current_page);
                break;
            case IRON:
                BackPack goldBackpack = createBackPack(player, 36, "Gold Backpack", old_id);
                goldBackpack.setFirst_page(old_first_page);
                goldBackpack.setSecond_page(old_second_page);
                goldBackpack.setCurrent_page(old_current_page);
                break;
            case GOLD:
                BackPack lapisBackpack = createBackPack(player, 45, "Lapis Backpack", old_id);
                lapisBackpack.setFirst_page(old_first_page);
                lapisBackpack.setSecond_page(old_second_page);
                lapisBackpack.setCurrent_page(old_current_page);
                break;
            case LAPIS:
                BackPack amethystBackpack = createBackPack(player, 54, "Amethyst Backpack", old_id);
                amethystBackpack.setFirst_page(old_first_page);
                amethystBackpack.setSecond_page(old_second_page);
                amethystBackpack.setCurrent_page(old_current_page);
                break;
            case AMETHYST:
                BackPack diamondBackpack = createBackPack(player, 81, "Diamond Backpack", old_id);
                diamondBackpack.setFirst_page(old_first_page);
                diamondBackpack.setSecond_page(old_second_page);
                diamondBackpack.setCurrent_page(old_current_page);
                break;
            case DIAMOND:
                BackPack netheriteBackpack = createBackPack(player, 108, "Netherite Backpack", old_id);
                netheriteBackpack.setFirst_page(old_first_page);
                netheriteBackpack.setSecond_page(old_second_page);
                netheriteBackpack.setCurrent_page(old_current_page);
                break;
        }

    }

    public void remove_backpack(Player player, BackPack backPack) {
        playerBackPacks.get(player).remove(backPack);
    }

    public List<BackPack> getPlayerBackPacks(Player player) {
        // Obtenha a lista de mochilas do jogador
        return playerBackPacks.getOrDefault(player, new ArrayList<>());
    }
}

