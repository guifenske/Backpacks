package br.com.backpacks.upgrades;

import br.com.backpacks.menu.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VillagerBaitUpgrade extends Upgrade {
    private boolean enabled;
    public Inventory getInventory() {
        return inventory;
    }
    private final Inventory inventory;

    public VillagerBaitUpgrade(int id){
        super(UpgradeType.VILLAGER_BAIT, id);
        this.enabled = false;
        this.inventory = Bukkit.createInventory(null, 27, "§6§lVillagers Follow");
        updateInventory();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void updateInventory(){
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").build();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").build();

        if(this.enabled){
            inventory.setItem(13, disable);
        }   else{
            inventory.setItem(13, enable);
        }
    }
}
