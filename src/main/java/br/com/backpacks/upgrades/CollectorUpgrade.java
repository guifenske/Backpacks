package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CollectorUpgrade extends Upgrade {
    //mode 0 = only backpack items, mode 1 = every item
    private int mode;
    private boolean enabled;
    private final Inventory inventory;
    public Inventory getInventory() {
        return inventory;
    }

    public CollectorUpgrade(int id){
        super(UpgradeType.COLLECTOR, id);
        this.enabled = false;
        this.mode = 0;
        this.inventory = Bukkit.createInventory(null, 27, "§6Collector: mode " + this.mode);
        updateInventory();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void updateInventory(){
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();
        ItemStack mode0 = new ItemCreator(Material.LIME_STAINED_GLASS_PANE, "Mode 0: Only pickup backpack stored items").get();
        ItemStack mode1 = new ItemCreator(Material.BLUE_STAINED_GLASS_PANE, "Mode 1: Store all pickup-ed items into the backpack").get();

        if(this.isEnabled()) {
            this.inventory.setItem(11, disable);
        }   else {
            this.inventory.setItem(11, enable);
        }

        if(this.mode == 0){
            inventory.setItem(13, mode1);
        }   else {
            inventory.setItem(13, mode0);
        }
    }
}
