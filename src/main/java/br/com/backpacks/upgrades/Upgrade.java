package br.com.backpacks.upgrades;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public Inventory getInventory() {
        return null;
    }

    public boolean isAdvanced(){return false;}

    public List<Integer> inputSlots(){return null;}

    public List<Integer> outputSlots(){return null;}

    public ItemStack tryAddItem(@NotNull List<Integer> slots, @NotNull ItemStack itemStack){
        Inventory inventory = getInventory();
        int amount = itemStack.getAmount();
        ItemStack itemStack1 = itemStack.clone();
        itemStack1.setAmount(1);

        for(int i : slots){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, itemStack1);
                return null;
            }

            if(inventory.getItem(i).getAmount() == inventory.getItem(i).getMaxStackSize()) continue;

            if(inventory.getItem(i).isSimilar(itemStack1) && inventory.getItem(i).getAmount() + amount <= itemStack1.getMaxStackSize()){
                inventory.getItem(i).setAmount(amount + inventory.getItem(i).getAmount());
                return null;
            }

            else if(inventory.getItem(i).isSimilar(itemStack1) && inventory.getItem(i).getAmount() + amount > itemStack1.getMaxStackSize()){
                amount = (inventory.getItem(i).getAmount() + amount) - itemStack1.getMaxStackSize();
                inventory.getItem(i).setAmount(itemStack1.getMaxStackSize());
            }
        }

        itemStack1.setAmount(amount);
        return itemStack1;
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

    public void stopTicking(){};
}
