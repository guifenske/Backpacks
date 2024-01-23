package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static br.com.backpacks.events.upgrades.AutoFeed.fillSlots;

public class AutoFeedUpgrade extends Upgrade {
    private boolean enabled;

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

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
        this.inventory = Bukkit.createInventory(null, 27, "Auto Feed");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void updateInventory(){
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, "Put your food in the empty 9x9 space").get();
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();


        for (int i = 0; i < 27; i++) {
            if(!fillSlots.contains(i)) inventory.setItem(i, blank);
        }

        if(isEnabled())    inventory.setItem(10, disable);
        else{
            setEnabled(false);
            inventory.setItem(10, enable);
        }

        int i1 = 0;
        if(getItems() != null && !getItems().isEmpty()){
            for(int i : fillSlots){
                if(i1 >= getItems().size()) break;
                inventory.setItem(i, getItems().get(i1));
                i1++;
            }
        }
    }
}
