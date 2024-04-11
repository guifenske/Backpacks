package br.com.backpacks.utils;

import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Upgrade {
    private final UpgradeType type;
    private final int id;

    public Upgrade(UpgradeType type, int id) {
        this.type = type;
        this.id = id;
    }

    public UpgradeType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public Inventory getInventory() {
        return null;
    }

    public boolean canBeInputOrOutputHolder(){return false;}

    public boolean isAdvanced(){return false;}

    public List<Integer> inputSlots(){return null;}

    public List<Integer> outputSlots(){return null;}

    public ItemStack tryAddItem(@NotNull List<Integer> slots, @NotNull ItemStack itemStack){
        Inventory inventory = getInventory();
        int amount = itemStack.getAmount();
        for(int i : slots){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, itemStack);
                return null;
            }

            if(!inventory.getItem(i).isSimilar(itemStack)) continue;
            if(inventory.getItem(i).getAmount() == inventory.getItem(i).getMaxStackSize()) continue;

            if(inventory.getItem(i).getAmount() + amount <= itemStack.getMaxStackSize()){
                inventory.getItem(i).add(itemStack.getAmount());
                return null;
            }

            amount = (inventory.getItem(i).getAmount() + amount) - itemStack.getMaxStackSize();
            itemStack.setAmount(amount);
            inventory.getItem(i).setAmount(itemStack.getMaxStackSize());
        }

        return itemStack;
    }

    public ItemStack getFirstOutputItem(){
        Inventory inventory = getInventory();
        for(int i : outputSlots()){
            if(inventory.getItem(i) == null) continue;
            return inventory.getItem(i);
        }

        return null;
    }

    public boolean canReceiveSpecificItemAsInput(@NotNull ItemStack itemStack){return false;}

    public void stopTickingUpgrade(){
        closeInventoryGlobally();
    };

    public void closeInventoryGlobally(){
        if(getInventory() == null) return;
        for(HumanEntity viewer : getInventory().getViewers()){
            BackpackAction.clearPlayerAction(viewer);
            viewer.closeInventory();
        }
    }
}
