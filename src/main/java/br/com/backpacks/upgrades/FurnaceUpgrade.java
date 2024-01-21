package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceUpgrade extends Upgrade {
    private ItemStack result;
    private ItemStack fuel;
    private ItemStack smelting;
    private int lastMaxOperation = -1;

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public int getLastMaxOperation() {
        return lastMaxOperation;
    }

    public void setLastMaxOperation(int lastMaxOperation) {
        this.lastMaxOperation = lastMaxOperation;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    private int operation = 0;

    public long getCookItemTicks() {
        return cookItemTicks;
    }

    private final long cookItemTicks;

    public FurnaceUpgrade(int id, UpgradeType upgradeType){
        super(upgradeType, id);
        if(upgradeType.equals(UpgradeType.BLAST_FURNACE)){
            this.cookItemTicks = 100L;
            this.inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE);
        }   else if(upgradeType.equals(UpgradeType.SMOKER)){
            this.cookItemTicks = 100L;
            this.inventory = Bukkit.createInventory(null, InventoryType.SMOKER);
        }   else{
            this.cookItemTicks = 200L;
            this.inventory = Bukkit.createInventory(null, InventoryType.FURNACE);
        }
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    public ItemStack getSmelting() {
        return smelting;
    }

    public void setSmelting(ItemStack smelting) {
        this.smelting = smelting;
    }

    public boolean canTick(){
        if(getSmelting() == null)   return false;
        if(getResult() != null && getResult().getAmount() == getResult().getMaxStackSize()) return false;
        if(getFuel() != null && operation >= 0) return true;
        if(operation == 0 && getFuel() == null)    return false;
        if(operation > 0 && getFuel() == null) return true;
        return false;
    }

    public void updateInventory(){
        inventory.setItem(0, getSmelting());
        inventory.setItem(1, getFuel());
        inventory.setItem(2, getResult());
    }
}
