package br.com.backpacks.backpackUtils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Upgrade {
    private final UpgradeType type;
    private final int id;
    public Upgrade(UpgradeType type, int id) {
        this.type = type;
        this.id = id;
    }

    private Inventory inventory;

    public UpgradeType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    private Block block = null;

    public Block getBoundFakeBlock() {
        return block;
    }

    public void setBoundFakeBlock(Block block) {
        this.block = block;
    }

    private List<Player> viewers = new ArrayList<>();

    public List<Player> getViewers() {
        return viewers;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
