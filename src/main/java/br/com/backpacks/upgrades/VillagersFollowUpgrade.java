package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VillagersFollowUpgrade extends Upgrade {

    private boolean enabled;

    public Inventory getInventory() {
        return inventory;
    }

    private final Inventory inventory;


    public VillagersFollowUpgrade(int id){
        super(UpgradeType.VILLAGERSFOLLOW, id);
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
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();

        if(this.enabled){
            inventory.setItem(13, enable);
        }   else inventory.setItem(13, disable);
    }
}
