package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumMap;
import java.util.HashMap;

public class FurnaceGrid implements Listener {

    private static final HashMap<Integer, BukkitTask> taskMap = new HashMap<>();

    private static final IntOpenHashSet firstSave = new IntOpenHashSet();
    private static final EnumMap<Material, Fuel> fuelMap = new EnumMap<>(Material.class);
    private static final EnumMap<Fuel, Integer> maxOperationsMap = new EnumMap<>(Fuel.class);

    static {
        fuelMap.put(Material.COAL, Fuel.COAL);
        fuelMap.put(Material.COAL_BLOCK, Fuel.COAL_BLOCK);
        fuelMap.put(Material.BLAZE_ROD, Fuel.BLAZE_ROD);
        fuelMap.put(Material.ACACIA_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.BAMBOO_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.BIRCH_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.CHERRY_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.CRIMSON_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.OAK_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.DARK_OAK_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.JUNGLE_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.MANGROVE_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.SPRUCE_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.WARPED_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.STICK, Fuel.STICK);
        fuelMap.put(Material.ACACIA_BOAT, Fuel.BOAT);
        fuelMap.put(Material.ACACIA_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.BIRCH_BOAT, Fuel.BOAT);
        fuelMap.put(Material.BIRCH_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.CHERRY_BOAT, Fuel.BOAT);
        fuelMap.put(Material.CHERRY_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.DARK_OAK_BOAT, Fuel.BOAT);
        fuelMap.put(Material.DARK_OAK_CHEST_BOAT, Fuel.BOAT);

        maxOperationsMap.put(Fuel.COAL, 8);
        maxOperationsMap.put(Fuel.CHARCOAL, 8);
        maxOperationsMap.put(Fuel.COAL_BLOCK, 80);
        maxOperationsMap.put(Fuel.LAVA_BUCKET, 100);
        maxOperationsMap.put(Fuel.DRIED_KELP_BLOCK, 20);
        maxOperationsMap.put(Fuel.STICK, 1);
        maxOperationsMap.put(Fuel.BOWL, 1);
        maxOperationsMap.put(Fuel.WOODEN_EQUIPMENT, 1);
        maxOperationsMap.put(Fuel.PLANKS, 1);
        maxOperationsMap.put(Fuel.BLAZE_ROD, 12);
        maxOperationsMap.put(Fuel.BOAT, 6);
    }

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, InventoryType.FURNACE);

        inventory.setItem(1, backPack.getFuel());
        inventory.setItem(0, backPack.getSmelting());
        inventory.setItem(2, backPack.getResult());

        if(!firstSave.contains(backPack.getId())){
            firstSave.add(backPack.getId());
            updateFurnace(backPack.getId(), player);
        }

        return inventory;
    }

    private static void updateFurnace(int id, Player player){

        if(Main.backPackManager.getBackpackFromId(id) == null){
            return;
        }
        BukkitTask task = new BukkitRunnable() {
            int operation = 0;
            int lastMaxOperation = 0;
            @Override
            public void run() {
                BackPack backPack = Main.backPackManager.getBackpackFromId(id);

                if(backPack.getSmelting() == null){
                    operation = 0;
                    return;
                }

                if(backPack.getFuel() == null)    return;

                Fuel fuel = getFuelFromItem(backPack.getFuel());
                int maxOperation = getMaxOperationsFromFuel(fuel);

                if(fuel == Fuel.NOTHING){
                    //check to update the lava_bucket fuel status
                    if(lastMaxOperation > 0){
                        if(lastMaxOperation == 100 && backPack.getFuel().getType().equals(Material.BUCKET)){
                            maxOperation = 100;
                            lastMaxOperation = maxOperation;
                        }   else return;
                    }   else return;
                }

                if(lastMaxOperation > 0){
                    if(lastMaxOperation != maxOperation) operation = 0;
                }

                lastMaxOperation = maxOperation;

                if(backPack.getResult() != null){
                    if(backPack.getResult().getAmount() == backPack.getResult().getMaxStackSize()) return;
                }

                for(FurnaceRecipe recipe : Main.getMain().getFurnaceRecipes()){
                    if(!recipe.getInputChoice().test(backPack.getSmelting())) continue;

                    if(backPack.getResult() == null){
                        if(operation == 0){
                            if(maxOperation != 1)   operation = 1;
                            if(maxOperation == 100) backPack.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(backPack.getFuel() == null || backPack.getFuel().getAmount() == 1) backPack.setFuel(null);
                                else    backPack.setFuel(backPack.getFuel().subtract());
                            }
                        }
                        else{
                            if(operation >= maxOperation - 1){
                                operation = 0;
                                if(maxOperation == 100) backPack.setFuel(new ItemStack(Material.BUCKET));
                                else{
                                    if(backPack.getFuel() == null || backPack.getFuel().getAmount() == 1) backPack.setFuel(null);
                                    else    backPack.setFuel(backPack.getFuel().subtract());
                                }
                            }   else operation++;
                        }
                        backPack.setSmelting(backPack.getSmelting().subtract());
                        backPack.setResult(recipe.getResult());
                        updateOpenInv(backPack, player);
                        return;
                    }

                    if(!backPack.getResult().isSimilar(recipe.getResult())) return;

                    if(operation == 0){
                        if(maxOperation != 1)    operation = 1;
                        if(maxOperation == 100) backPack.setFuel(new ItemStack(Material.BUCKET));
                        else{
                            if(backPack.getFuel() == null || backPack.getFuel().getAmount() == 1) backPack.setFuel(null);
                            else    backPack.setFuel(backPack.getFuel().subtract());
                        }
                    }
                    else{
                        if(operation >= maxOperation - 1){
                            operation = 0;
                            if(maxOperation == 100) backPack.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(backPack.getFuel() == null || backPack.getFuel().getAmount() == 1) backPack.setFuel(null);
                                else    backPack.setFuel(backPack.getFuel().subtract());
                            }
                        }   else operation++;
                    }

                    backPack.setSmelting(backPack.getSmelting().subtract());
                    backPack.setResult(backPack.getResult().add());
                    updateOpenInv(backPack, player);
                    return;
                }
            }
        }.runTaskTimer(Main.getMain(), 0L, 200L);

       taskMap.put(id, task);

    }

    private static void updateOpenInv(BackPack backPack, Player player){
        if(!BackpackAction.getAction(player).equals(BackpackAction.Action.UPGFURNACE)) return;

        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.setItem(1, backPack.getFuel());
        inventory.setItem(0, backPack.getSmelting());
        inventory.setItem(2, backPack.getResult());
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == event.getInventory()) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGFURNACE)) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.setFuel(event.getInventory().getItem(1));
                backPack.setSmelting(event.getInventory().getItem(0));
                backPack.setResult(event.getInventory().getItem(2));
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));

        if(!firstSave.contains(backPack.getId())){
            firstSave.add(backPack.getId());
            backPack.setFuel(event.getInventory().getItem(1));
            backPack.setSmelting(event.getInventory().getItem(0));
            backPack.setResult(event.getInventory().getItem(2));
        }

        if(backPack.getFuel() == null || backPack.getSmelting() == null){
            taskMap.get(backPack.getId()).cancel();
            taskMap.remove(backPack.getId());
            firstSave.remove(backPack.getId());
        }

        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);
        new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTask(Main.getMain());
    }

    public enum Fuel {
        COAL,
        COAL_BLOCK,
        BLAZE_ROD,
        PLANKS,
        STICK,
        BOAT,
        CHARCOAL,
        LAVA_BUCKET,
        DRIED_KELP_BLOCK,
        WOODEN_EQUIPMENT,
        BOWL,
        NOTHING;
    }

    private static Fuel getFuelFromItem(ItemStack item){
        if(item == null) return Fuel.NOTHING;
        return fuelMap.getOrDefault(item.getType(), Fuel.NOTHING);
    }

    private static int getMaxOperationsFromFuel(Fuel fuel){
        return maxOperationsMap.getOrDefault(fuel, 0);
    }
}
