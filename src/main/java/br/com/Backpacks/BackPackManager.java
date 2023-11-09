package br.com.Backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackPackManager {
    private Map<Player, List<BackPack>> playerBackPacks = new HashMap<>();

    public void createBackPack(Player player, int size, String name) {
        // Create a new backpack with the specified size and name
        BackPack backPack = new BackPack(size, name, Bukkit.createInventory(player, size, name));

        // Add the backpack to the player's list of backpacks
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
