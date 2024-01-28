package br.com.backpacks.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.events.upgrades.Furnace;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FurnaceUpgrade extends Upgrade {
    private ItemStack result;
    private ItemStack fuel;
    private ItemStack smelting;
    private int lastMaxOperation = -1;
    private final Inventory inventory;

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    private final int cookTimeMultiplier;

    private int cookTime = 0;

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

    public Block getBoundFakeBlock() {
        return boundFakeBlock;
    }

    public void setBoundFakeBlock(Block boundFakeBlock) {
        this.boundFakeBlock = boundFakeBlock;
    }

    private Block boundFakeBlock;

    public long getCookItemTicks() {
        return cookItemTicks;
    }

    private final long cookItemTicks;

    public FurnaceUpgrade(int id, UpgradeType upgradeType){
        super(upgradeType, id);
        if(upgradeType.equals(UpgradeType.BLAST_FURNACE)){
            this.cookItemTicks = 100L;
            this.inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE);
            this.cookTimeMultiplier = 4;
        }   else if(upgradeType.equals(UpgradeType.SMOKER)){
            this.cookItemTicks = 100L;
            this.inventory = Bukkit.createInventory(null, InventoryType.SMOKER);
            this.cookTimeMultiplier = 4;
        }   else{
            this.cookItemTicks = 200L;
            this.inventory = Bukkit.createInventory(null, InventoryType.FURNACE);
            this.cookTimeMultiplier = 2;
        }
        updateInventory();
    }

    public BukkitTask getSubTickTask() {
        return subTickTask;
    }

    private BukkitTask subTickTask = null;

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

    public void startSubTick(){
        subTickTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!Furnace.shouldTick.contains(getId())){
                    return;
                }
                if(!canTick()){
                    return;
                }
                setCookTime(getCookTime() + cookTimeMultiplier);
                for(Player player : getViewers()){
                    InventoryView view = player.getOpenInventory();
                    view.setProperty(InventoryView.Property.COOK_TIME, getCookTime());
                }
            }
        }.runTaskTimer(Main.getMain(), 0L, 2L);
    }

    public Inventory getInventory(){
        return inventory;
    }
}
