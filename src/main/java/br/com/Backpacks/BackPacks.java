package br.com.Backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class BackPacks {
    private Player owner;

    private List<Inventory> inventories = new ArrayList<>();

    public BackPacks(Player player){
        owner = player;
    }

    public void create_backpack(String string){
        Inventory inventory = Bukkit.createInventory(owner, 54, string);

        inventories.add(inventory);
    }

    public Inventory get_backpack(int id){
        return inventories.get(id);
    }
}
