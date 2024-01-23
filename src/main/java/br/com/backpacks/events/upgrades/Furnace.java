package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.events.custom.BackpackCookItemEvent;
import br.com.backpacks.upgrades.BlastFurnaceUpgrade;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.upgrades.SmokerUpgrade;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

public class Furnace implements Listener {
    public static final HashMap<UUID, FurnaceUpgrade> currentFurnace = new HashMap<>();
    private static final HashMap<Integer, BukkitTask> taskMap = new HashMap<>();
    public static final IntOpenHashSet shouldTick = new IntOpenHashSet();
    private static final EnumMap<Material, Fuel> fuelMap = new EnumMap<>(Material.class);
    private static final EnumMap<Fuel, Integer> maxOperationsMap = new EnumMap<>(Fuel.class);

    static {
        fuelMap.put(Material.COAL, Fuel.COAL);
        fuelMap.put(Material.CHARCOAL, Fuel.CHARCOAL);
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
        fuelMap.put(Material.JUNGLE_BOAT, Fuel.BOAT);
        fuelMap.put(Material.JUNGLE_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.MANGROVE_BOAT, Fuel.BOAT);
        fuelMap.put(Material.MANGROVE_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.OAK_BOAT, Fuel.BOAT);
        fuelMap.put(Material.OAK_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.SPRUCE_BOAT, Fuel.BOAT);
        fuelMap.put(Material.SPRUCE_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.WOODEN_AXE, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_SHOVEL, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_SWORD, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_HOE, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_PICKAXE, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.BOWL, Fuel.BOWL);
        fuelMap.put(Material.ACACIA_LOG, Fuel.LOGS);
        fuelMap.put(Material.BIRCH_LOG, Fuel.LOGS);
        fuelMap.put(Material.CHERRY_LOG, Fuel.LOGS);
        fuelMap.put(Material.DARK_OAK_LOG, Fuel.LOGS);
        fuelMap.put(Material.JUNGLE_LOG, Fuel.LOGS);
        fuelMap.put(Material.MANGROVE_LOG, Fuel.LOGS);
        fuelMap.put(Material.OAK_LOG, Fuel.LOGS);
        fuelMap.put(Material.SPRUCE_LOG, Fuel.LOGS);
        fuelMap.put(Material.STRIPPED_ACACIA_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_BIRCH_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_CHERRY_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_DARK_OAK_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_JUNGLE_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_MANGROVE_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_OAK_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_SPRUCE_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_ACACIA_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_BIRCH_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_CHERRY_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_DARK_OAK_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_JUNGLE_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_MANGROVE_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_OAK_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_SPRUCE_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.ACACIA_WOOD, Fuel.WOOD);
        fuelMap.put(Material.BIRCH_WOOD, Fuel.WOOD);
        fuelMap.put(Material.CHERRY_WOOD, Fuel.WOOD);
        fuelMap.put(Material.DARK_OAK_WOOD, Fuel.WOOD);
        fuelMap.put(Material.JUNGLE_WOOD, Fuel.WOOD);
        fuelMap.put(Material.MANGROVE_WOOD, Fuel.WOOD);
        fuelMap.put(Material.OAK_WOOD, Fuel.WOOD);
        fuelMap.put(Material.SPRUCE_WOOD, Fuel.WOOD);
        fuelMap.put(Material.ACACIA_SIGN, Fuel.SIGN);
        fuelMap.put(Material.BIRCH_SIGN, Fuel.SIGN);
        fuelMap.put(Material.CHERRY_SIGN, Fuel.SIGN);
        fuelMap.put(Material.DARK_OAK_SIGN, Fuel.SIGN);
        fuelMap.put(Material.JUNGLE_SIGN, Fuel.SIGN);
        fuelMap.put(Material.MANGROVE_SIGN, Fuel.SIGN);
        fuelMap.put(Material.OAK_SIGN, Fuel.SIGN);
        fuelMap.put(Material.SPRUCE_SIGN, Fuel.SIGN);
        fuelMap.put(Material.ACACIA_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.BIRCH_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.CHERRY_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.DARK_OAK_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.JUNGLE_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.MANGROVE_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.OAK_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.SPRUCE_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.ACACIA_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.BIRCH_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.CHERRY_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.DARK_OAK_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.JUNGLE_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.OAK_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.SPRUCE_SAPLING, Fuel.SAPLING);

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
        maxOperationsMap.put(Fuel.LOGS, 2);
        maxOperationsMap.put(Fuel.STRIPPED_LOGS, 2);
        maxOperationsMap.put(Fuel.STRIPPED_WOOD, 2);
        maxOperationsMap.put(Fuel.WOOD, 2);
        maxOperationsMap.put(Fuel.SIGN, 1);
        maxOperationsMap.put(Fuel.HANGING_SIGN, 1);
        maxOperationsMap.put(Fuel.SAPLING, 1);
    }

    public static void tick(FurnaceUpgrade upgrade){
        if(upgrade == null){
            return;
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(upgrade.getSmelting() == null){
                    upgrade.setOperation(0);
                    return;
                }

                if(upgrade.getFuel() == null && upgrade.getOperation() == 0)    return;

                Fuel fuel = getFuelFromItem(upgrade.getFuel());
                int maxOperation = getMaxOperationsFromFuel(fuel);
                if(fuel.equals(Fuel.NOTHING)){
                    if(upgrade.getLastMaxOperation() > 0 && upgrade.getOperation() > 0){
                        if(upgrade.getLastMaxOperation() == 100 && upgrade.getFuel().getType().equals(Material.BUCKET)){
                            maxOperation = 100;
                            upgrade.setLastMaxOperation(maxOperation);
                        }   else    maxOperation = upgrade.getLastMaxOperation();
                    }   else   return;
                }

                if(upgrade.getLastMaxOperation() > 0){
                    if(upgrade.getLastMaxOperation() != maxOperation) upgrade.setOperation(0);
                }

                upgrade.setLastMaxOperation(maxOperation);

                if(upgrade.getResult() != null){
                    if(upgrade.getResult().getAmount() == upgrade.getResult().getMaxStackSize()){
                        upgrade.setOperation(0);
                        return;
                    }
                }

                if(upgrade.getType().equals(UpgradeType.SMOKER)){
                    smokerLogic((SmokerUpgrade) upgrade, maxOperation);
                }   else if(upgrade.getType().equals(UpgradeType.BLAST_FURNACE)){
                    blastFurnaceLogic((BlastFurnaceUpgrade) upgrade, maxOperation);
                }   else {
                    furnaceLogic(upgrade, maxOperation);
                }
            }
        }.runTaskTimer(Main.getMain(), upgrade.getCookItemTicks(), upgrade.getCookItemTicks());
       taskMap.put(upgrade.getId(), task);
       Main.getMain().debugMessage("Starting furnace task for " + upgrade.getId() + "!");
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGFURNACE)) return;
        UUID id = event.getWhoClicked().getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                currentFurnace.get(id).setFuel(event.getInventory().getItem(1));
                currentFurnace.get(id).setSmelting(event.getInventory().getItem(0));
                currentFurnace.get(id).setResult(event.getInventory().getItem(2));
                if(!shouldTick.contains(currentFurnace.get(id).getId())) {
                    if (currentFurnace.get(id).canTick()) {
                        shouldTick.add(currentFurnace.get(id).getId());
                        tick(currentFurnace.get(id));
                    }
                }   else{
                    if(!currentFurnace.get(id).canTick()){
                        taskMap.get(currentFurnace.get(id).getId()).cancel();
                        shouldTick.remove(currentFurnace.get(id).getId());
                    }
                }
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        UUID id = event.getPlayer().getUniqueId();

        currentFurnace.get(id).setFuel(event.getInventory().getItem(1));
        currentFurnace.get(id).setSmelting(event.getInventory().getItem(0));
        currentFurnace.get(id).setResult(event.getInventory().getItem(2));

        if(!currentFurnace.get(id).canTick()){
            if(taskMap.containsKey(currentFurnace.get(id).getId())) {
                taskMap.get(currentFurnace.get(id).getId()).cancel();
                taskMap.remove(currentFurnace.get(id).getId());
            }
            shouldTick.remove(currentFurnace.get(id).getId());
            currentFurnace.remove(id);
        }

        BackpackAction.removeAction((Player) event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);

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
        LOGS,
        WOOD,
        STRIPPED_WOOD,
        STRIPPED_LOGS,
        SIGN,
        HANGING_SIGN,
        SAPLING,
        NOTHING
    }

    private static Fuel getFuelFromItem(ItemStack item){
        if(item == null) return Fuel.NOTHING;
        return fuelMap.getOrDefault(item.getType(), Fuel.NOTHING);
    }

    private static int getMaxOperationsFromFuel(Fuel fuel){
        return maxOperationsMap.getOrDefault(fuel, 0);
    }

    private static void furnaceLogic(FurnaceUpgrade upgrade, int maxOperation){
        for(FurnaceRecipe recipe : Main.getMain().getFurnaceRecipes()){
            if(!recipe.getInputChoice().test(upgrade.getSmelting())) continue;

            if(upgrade.getResult() == null){
                BackpackCookItemEvent e = new BackpackCookItemEvent(upgrade.getSmelting(), recipe.getResult(), upgrade.getFuel(), false);
                Bukkit.getPluginManager().callEvent(e);

                if(!e.isCancelled()) {
                    if(upgrade.getOperation() == 0){
                        if(maxOperation != 1)   upgrade.setOperation(1);
                        if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else{
                            if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else    upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }
                    else{
                        if(upgrade.getOperation() >= maxOperation - 1){
                            upgrade.setOperation(0);
                            if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                                else    upgrade.setFuel(upgrade.getFuel().subtract());
                            }
                        }   else upgrade.setOperation(upgrade.getOperation() + 1);
                    }
                    upgrade.setResult(e.getResult());
                    upgrade.setSmelting(e.getSource().subtract());
                    upgrade.updateInventory();
                }

                return;
            }

            if(!upgrade.getResult().isSimilar(recipe.getResult())) return;
            BackpackCookItemEvent e = new BackpackCookItemEvent(upgrade.getSmelting(), upgrade.getResult(), upgrade.getFuel(), true);
            Bukkit.getPluginManager().callEvent(e);

            if(!e.isCancelled()) {
                if (upgrade.getOperation() == 0) {
                    if (maxOperation != 1) upgrade.setOperation(1);
                    if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                    else {
                        if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1)
                            upgrade.setFuel(null);
                        else upgrade.setFuel(upgrade.getFuel().subtract());
                    }
                } else {
                    if (upgrade.getOperation() >= maxOperation - 1) {
                        upgrade.setOperation(0);
                        if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else {
                            if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1)
                                upgrade.setFuel(null);
                            else upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    } else upgrade.setOperation(upgrade.getOperation() + 1);
                }

                upgrade.setResult(e.getResult().add());
                upgrade.setSmelting(e.getSource().subtract());
                upgrade.updateInventory();
            }
            return;
        }
    }

    private static void smokerLogic(SmokerUpgrade upgrade, int maxOperation){
        for(SmokingRecipe recipe : Main.getMain().getSmokingRecipes()){
            if(!recipe.getInputChoice().test(upgrade.getSmelting())) continue;

            if(upgrade.getResult() == null){
                BackpackCookItemEvent e = new BackpackCookItemEvent(upgrade.getSmelting(), recipe.getResult(), upgrade.getFuel(), false);
                Bukkit.getPluginManager().callEvent(e);

                if(!e.isCancelled()) {
                    if(upgrade.getOperation() == 0){
                        if(maxOperation != 1)   upgrade.setOperation(1);
                        if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else{
                            if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else    upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }
                    else{
                        if(upgrade.getOperation() >= maxOperation - 1){
                            upgrade.setOperation(0);
                            if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                                else    upgrade.setFuel(upgrade.getFuel().subtract());
                            }
                        }   else upgrade.setOperation(upgrade.getOperation() + 1);
                    }
                    upgrade.setResult(e.getResult());
                    upgrade.setSmelting(e.getSource().subtract());
                    upgrade.updateInventory();
                }

                return;
            }

            if(!upgrade.getResult().isSimilar(recipe.getResult())) return;
            BackpackCookItemEvent e = new BackpackCookItemEvent(upgrade.getSmelting(), upgrade.getResult(), upgrade.getFuel(), true);
            Bukkit.getPluginManager().callEvent(e);

            if(!e.isCancelled()) {
                if (upgrade.getOperation() == 0) {
                    if (maxOperation != 1) upgrade.setOperation(1);
                    if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                    else {
                        if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1)
                            upgrade.setFuel(null);
                        else upgrade.setFuel(upgrade.getFuel().subtract());
                    }
                } else {
                    if (upgrade.getOperation() >= maxOperation - 1) {
                        upgrade.setOperation(0);
                        if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else {
                            if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1)
                                upgrade.setFuel(null);
                            else upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    } else upgrade.setOperation(upgrade.getOperation() + 1);
                }

                upgrade.setResult(e.getResult().add());
                upgrade.setSmelting(e.getSource().subtract());
                upgrade.updateInventory();
            }
            return;
        }
    }
    private static void blastFurnaceLogic(BlastFurnaceUpgrade upgrade, int maxOperation){
        for(BlastingRecipe recipe : Main.getMain().getBlastingRecipes()){
            if(!recipe.getInputChoice().test(upgrade.getSmelting())) continue;

            if(upgrade.getResult() == null){
                BackpackCookItemEvent e = new BackpackCookItemEvent(upgrade.getSmelting(), recipe.getResult(), upgrade.getFuel(), false);
                Bukkit.getPluginManager().callEvent(e);

                if(!e.isCancelled()) {
                    if(upgrade.getOperation() == 0){
                        if(maxOperation != 1)   upgrade.setOperation(1);
                        if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else{
                            if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else    upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }
                    else{
                        if(upgrade.getOperation() >= maxOperation - 1){
                            upgrade.setOperation(0);
                            if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                                else    upgrade.setFuel(upgrade.getFuel().subtract());
                            }
                        }   else upgrade.setOperation(upgrade.getOperation() + 1);
                    }
                    upgrade.setResult(e.getResult());
                    upgrade.setSmelting(e.getSource().subtract());
                    upgrade.updateInventory();
                }

                return;
            }

            if(!upgrade.getResult().isSimilar(recipe.getResult())) return;
            BackpackCookItemEvent e = new BackpackCookItemEvent(upgrade.getSmelting(), upgrade.getResult(), upgrade.getFuel(), true);
            Bukkit.getPluginManager().callEvent(e);

            if(!e.isCancelled()) {
                if (upgrade.getOperation() == 0) {
                    if (maxOperation != 1) upgrade.setOperation(1);
                    if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                    else {
                        if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1)
                            upgrade.setFuel(null);
                        else upgrade.setFuel(upgrade.getFuel().subtract());
                    }
                } else {
                    if (upgrade.getOperation() >= maxOperation - 1) {
                        upgrade.setOperation(0);
                        if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else {
                            if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1)
                                upgrade.setFuel(null);
                            else upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    } else upgrade.setOperation(upgrade.getOperation() + 1);
                }

                upgrade.setResult(e.getResult().add());
                upgrade.setSmelting(e.getSource().subtract());
                upgrade.updateInventory();
            }
            return;
        }
    }
}
