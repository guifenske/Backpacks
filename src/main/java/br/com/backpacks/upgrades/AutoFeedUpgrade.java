package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AutoFeedUpgrade extends Upgrade {
    private boolean enabled;

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    private List<ItemStack> items;

    public AutoFeedUpgrade(int id){
        super(UpgradeType.AUTOFEED, id);
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
