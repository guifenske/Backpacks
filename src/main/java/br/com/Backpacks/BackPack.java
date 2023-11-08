package br.com.Backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class BackPack {
    private Player owner;

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public int getSize() {
        return size;
    }

    private int size;

    public String getName() {
        return name;
    }

    private String name;

    public BackPack(Player player){
        owner = player;
    }

    public BackPack(){

    }

    public AtomicReference<HashMap<Player, BackPack>> getBackpacks() {
        return backpacks;
    }

    private final AtomicReference<HashMap<Player, BackPack>> backpacks = new AtomicReference<>(new HashMap<>());

    public void create_backpack(int size, String name){
        this.inventory = Bukkit.createInventory(owner, size, name);
        this.name = name;
        this.size = size;
        backpacks.get().put(owner, this);
    }
}
