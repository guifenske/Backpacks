package br.com.backpacks.upgrades;

import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FilterUpgrade extends Upgrade {
    private final Inventory inventory;
    private final boolean isAdvanced;
    private List<ItemStack> filteredItems = new ArrayList<>();

    public FilterUpgrade(UpgradeType type, int id) {
        super(type, id);
        switch (type){
            case FILTER -> {
                inventory = Bukkit.createInventory(null, 9, "Filter Upgrade");
                isAdvanced = false;
                initInventory();
            }
            case ADVANCED_FILTER -> {
                inventory = Bukkit.createInventory(null, 9, "Advanced Filter Upgrade");
                isAdvanced = true;
            }
            default -> {
                isAdvanced = false;
                inventory = null;
            }
        }
    }

    private void initInventory(){
        ItemStack itemStack = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();
        for(int i = 0; i < inventory.getSize(); i++){
            inventory.setItem(i, itemStack);
        }
        inventory.setItem(4, null);
    }

    public List<ItemStack> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<ItemStack> filteredItems) {
        this.filteredItems = filteredItems;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isAdvanced() {
        return isAdvanced;
    }
}