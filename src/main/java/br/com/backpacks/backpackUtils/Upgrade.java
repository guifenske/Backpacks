package br.com.backpacks.backpackUtils;

import org.bukkit.inventory.Inventory;

public class Upgrade {

    private Inventory inventory;
    private final UpgradeType type;
    private final int id;
    public Upgrade(UpgradeType type, int id) {
        this.type = type;
        this.id = id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public UpgradeType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
