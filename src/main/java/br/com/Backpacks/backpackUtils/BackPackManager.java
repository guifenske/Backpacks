package br.com.Backpacks.backpackUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BackPackManager {
    public void setPlayerBackPacks(Player player, List<BackPack> backPacks) {
        playerBackPacks.put(player, backPacks);
    }

    private Map<Player, List<BackPack>> playerBackPacks = new ConcurrentHashMap<>();
    public Map<Player, List<Integer>> getBackpacks_ids() {
        return backpacks_ids;
    }

    private Map<Player, List<Integer>> backpacks_ids = new HashMap<>();

    public BackPack createBackPack(Player player, int size, String name, int id) {
        // Create a new backpack with the specified size and name

        if(size > 54){
            BackPack backPack = new BackPack(name, Bukkit.createInventory(player, 54, name), Bukkit.createInventory(player, size - 54, name), id);
            playerBackPacks.computeIfAbsent(player, k -> new ArrayList<>()).add(backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, Bukkit.createInventory(player, size, name), id);
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
        BackPack old_backpack = get_backpack_from_id(player, old_id);
        if(old_backpack == null){
            player.sendMessage("§cFailed to upgrade backpack!");
            return;
        }

        Inventory old_first_page = old_backpack.getFirst_page();
        Inventory old_second_page = old_backpack.getSecond_page();

        remove_backpack(player, old_backpack);

        switch (old_type){
            case LEATHER:
                BackPack ironBackpack = createBackPack(player, 27, "Iron Backpack", old_id);
                ironBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                ironBackpack.setCurrent_page(ironBackpack.getFirst_page());
                playerBackPacks.get(player).add(ironBackpack);
                break;
            case IRON:
                BackPack goldBackpack = createBackPack(player, 36, "Gold Backpack", old_id);
                goldBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                goldBackpack.setCurrent_page(goldBackpack.getFirst_page());
                playerBackPacks.get(player).add(goldBackpack);
                break;
            case GOLD:
                BackPack lapisBackpack = createBackPack(player, 45, "Lapis Backpack", old_id);
                lapisBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                lapisBackpack.setCurrent_page(lapisBackpack.getFirst_page());
                playerBackPacks.get(player).add(lapisBackpack);
                break;
            case LAPIS:
                BackPack amethystBackpack = createBackPack(player, 54, "Amethyst Backpack", old_id);
                amethystBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                amethystBackpack.setCurrent_page(amethystBackpack.getFirst_page());
                playerBackPacks.get(player).add(amethystBackpack);
                break;
            case AMETHYST:
                BackPack diamondBackpack = createBackPack(player, 81, "Diamond Backpack", old_id);
                diamondBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                diamondBackpack.setCurrent_page(diamondBackpack.getFirst_page());
                playerBackPacks.get(player).add(diamondBackpack);
                break;
            case DIAMOND:
                BackPack netheriteBackpack = createBackPack(player, 108, "Netherite Backpack", old_id);
                netheriteBackpack.getFirst_page().setStorageContents(old_first_page.getStorageContents());
                netheriteBackpack.getSecond_page().setStorageContents(old_second_page.getStorageContents());
                netheriteBackpack.setCurrent_page(netheriteBackpack.getFirst_page());
                playerBackPacks.get(player).add(netheriteBackpack);
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

