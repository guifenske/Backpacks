package br.com.Backpacks;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BackPack {
    public int getSize() {
        return size;
    }

    private Inventory inventory;

    public Inventory getInventory() {
        return inventory;
    }

    public void set_item(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    private int size;

    public String getName() {
        return name;
    }

    private String name;

    public BackPack(int size, String name, Inventory inventory) {
        this.size = size;
        this.name = name;
        this.inventory = inventory;
        // Crie a mochila com o tamanho e nome especificados
    }
}
