package br.com.backpacks.upgrades;

import br.com.backpacks.events.upgrades.AutoFeed;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static br.com.backpacks.events.upgrades.AutoFeed.fillSlots;

public class AutoFeedUpgrade extends Upgrade {
    private boolean enabled;

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public HashMap<Integer, ItemStack> getItems() {
        HashMap<Integer, ItemStack> hashMap = new HashMap<>();
        for(int i : fillSlots){
            hashMap.put(i, inventory.getItem(i));
        }
        return hashMap;
    }

    @Override
    public boolean canReceiveInput(@NotNull ItemStack itemStack) {
        return AutoFeed.checkFood(itemStack);
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }

    @Override
    public List<Integer> inputSlots() {
        return fillSlots;
    }

    @Override
    public List<Integer> outputSlots() {
        return fillSlots;
    }

    public AutoFeedUpgrade(int id){
        super(UpgradeType.AUTOFEED, id);
        this.enabled = false;
        this.inventory = Bukkit.createInventory(null, 27, "Auto Feed");
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, "Put your food in the empty 9x9 space").build();
        for (int i = 0; i < 27; i++) {
            if(!fillSlots.contains(i)) inventory.setItem(i, blank);
        }
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

        if(isEnabled())    inventory.setItem(10, disable);
        else{
            setEnabled(false);
            inventory.setItem(10, enable);
        }
    }
}
