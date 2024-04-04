package br.com.backpacks.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.FurnaceUpgradeEvents;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class FurnaceUpgrade extends Upgrade {
    private ItemStack result;
    private ItemStack fuel;
    private ItemStack smelting;
    private int lastMaxOperation = -1;
    private final Inventory inventory;
    private CookingRecipe<?> recipe;

    public CookingRecipe<?> getRecipe() {
        return recipe;
    }

    public void setRecipe(CookingRecipe<?> recipe) {
        this.recipe = recipe;
    }
    public int getCookTime() {
        return cookTime;
    }
    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
    private final int cookTimeAmount;
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
    public FurnaceUpgrade(UpgradeType upgradeType, int id){
        super(upgradeType, id);
        if(upgradeType.equals(UpgradeType.BLAST_FURNACE)){
            this.cookItemTicks = 100L;
            this.inventory = Bukkit.createInventory(null, InventoryType.BLAST_FURNACE);
            this.cookTimeAmount = 4;
        }   else if(upgradeType.equals(UpgradeType.SMOKER)){
            this.cookItemTicks = 100L;
            this.inventory = Bukkit.createInventory(null, InventoryType.SMOKER);
            this.cookTimeAmount = 4;
        }   else{
            this.cookItemTicks = 200L;
            this.inventory = Bukkit.createInventory(null, InventoryType.FURNACE);
            this.cookTimeAmount = 2;
        }
        updateInventory();
    }

    @Override
    public boolean canBeInputOrOutputHolder() {
        return true;
    }

    @Override
    public List<Integer> inputSlots() {
        return List.of(0, 1);
    }

    @Override
    public List<Integer> outputSlots() {
        return List.of(2);
    }

    @Override
    public void stopTickingUpgrade() {
        if(FurnaceUpgradeEvents.shouldTick.contains(getId())) FurnaceUpgradeEvents.shouldTick.remove(getId());
        setCookTime(0);
        if(FurnaceUpgradeEvents.taskMap.containsKey(getId())) FurnaceUpgradeEvents.taskMap.get(getId()).cancel();
        FurnaceUpgradeEvents.taskMap.remove(getId());
        setBoundFakeBlock(null);
        clearSubTickTask();
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

    public void clearSubTickTask(){
        for(HumanEntity player : getInventory().getViewers()){
            InventoryView view = player.getOpenInventory();
            view.setProperty(InventoryView.Property.COOK_TIME, 0);
        }
        if(subTickTask != null) subTickTask.cancel();
        subTickTask = null;
    }

    public void startSubTick(){
        subTickTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!FurnaceUpgradeEvents.shouldTick.contains(getId())){
                    this.cancel();
                    return;
                }
                if(!canTick()){
                    this.cancel();
                    return;
                }
                setCookTime(getCookTime() + cookTimeAmount);
                for(HumanEntity player : getInventory().getViewers()){
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
