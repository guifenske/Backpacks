package br.com.backpacks.upgrades;

import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AutoFeedUpgrade extends Upgrade {
    private boolean enabled;

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public AutoFeedUpgrade(int id){
        super(UpgradeType.AUTOFEED, id);
        this.enabled = false;
        this.inventory = Bukkit.createInventory(null, 27, "Auto Feed");
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

        if(isEnabled())    inventory.setItem(13, disable);
        else{
            inventory.setItem(13, enable);
        }
    }
}
