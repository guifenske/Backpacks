package br.com.backpacks.upgrades;

import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FurnaceUpgrade extends Upgrade {
    private ItemStack result;
    private ItemStack fuel;
    private ItemStack smelting;
    private int lastMaxOperation = -1;
    private Inventory inventory;
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

    public FurnaceUpgrade(int id){
        super(UpgradeType.FURNACE, id);

        this.cookItemTicks = 200L;
        this.inventory = null;
    }

    //todo: save location of the furnace so we can load it again, instead of creating a new one
    public void createFurnaceInventory(){
        Location tempLocation = new Location(Bukkit.getWorld("world"), ThreadLocalRandom.current().nextInt(-900, 900), -65, ThreadLocalRandom.current().nextInt(-900, 900));
        Block tempBlock = tempLocation.getBlock();

        tempBlock.setType(Material.FURNACE);
        setBoundFakeBlock(tempBlock);
        this.inventory = ((org.bukkit.block.Furnace) getBoundFakeBlock()).getInventory();
        updateInventory();
    }

    public BukkitTask getSubTickTask() {
        return subTickTask;
    }

    @Override
    public boolean isAdvanced() {
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
    public void stopTicking() {
        if(Furnace.isTicking.contains(getId())){
            Furnace.isTicking.remove((Integer) getId());
        }

        setCookTime(0);
        if(Furnace.taskMap.containsKey(getId())) Furnace.taskMap.get(getId()).cancel();
        Furnace.taskMap.remove(getId());
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

    public boolean canSmelt(){
        if(getSmelting() == null)   return false;
        if(getResult() != null && getResult().getAmount() == getResult().getMaxStackSize()) return false;
        if(getFuel() != null && operation >= 0) return true;
        if(operation == 0 && getFuel() == null)    return false;
        return operation > 0 && getFuel() == null;
    }

    public void updateInventory(){
        inventory.setItem(0, getSmelting());
        inventory.setItem(1, getFuel());
        inventory.setItem(2, getResult());
    }

    public void clearSubTickTask(){
        if(getBoundFakeBlock() == null) return;

        if(subTickTask != null) subTickTask.cancel();
        subTickTask = null;
    }

  /*  public void startSubTick(){

        subTickTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(!Furnace.isTicking.contains(getId())){
                    this.cancel();
                    return;
                }

                if(!canSmelt()){
                    this.cancel();
                    return;
                }

                if(getBoundFakeBlock() == null){
                    this.cancel();
                    return;
                }

                org.bukkit.block.Furnace furnace = (org.bukkit.block.Furnace) getBoundFakeBlock().getState();

                setCookTime(getCookTime() + cookTimeMultiplier);
                furnace.setCookTime((short) getCookTime());

            }
        }.runTaskTimer(Main.getMain(), 0L, 2L);
    }


   */

    public Inventory getInventory(){
        return inventory;
    }
}
