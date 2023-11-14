package br.com.Backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    public void setCurrent_backpack(Player player, BackPack current_backpack) {
        this.current_backpack.put(player, current_backpack);
        player.openInventory(current_backpack.getCurrent_page());
    }

    public BackPack getCurrent_backpack(Player player) {
        return current_backpack.get(player);
    }

    public void createBackPack(Player player, int size, String name, int id) {
        // Create a new backpack with the specified size and name

        if(size > 54){
            BackPack backPack = new BackPack(name, Bukkit.createInventory(player, 54, name), Bukkit.createInventory(player, size - 54, name), id);
            current_backpack.put(player, backPack);
            playerBackPacks.computeIfAbsent(player, k -> new ArrayList<>()).add(backPack);
            return;
        }

        BackPack backPack = new BackPack(name, Bukkit.createInventory(player, size, name), id);
        current_backpack.put(player, backPack);
        playerBackPacks.computeIfAbsent(player, k -> new ArrayList<>()).add(backPack);
    }

    public void remove_backpack(Player player, BackPack backPack) {
        playerBackPacks.get(player).remove(backPack);
    }

    public List<BackPack> getPlayerBackPacks(Player player) {
        // Obtenha a lista de mochilas do jogador
        return playerBackPacks.getOrDefault(player, new ArrayList<>());
    }
}
