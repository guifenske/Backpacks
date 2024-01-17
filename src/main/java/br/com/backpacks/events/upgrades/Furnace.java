package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.upgrades.FurnaceUpgrade;
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

public class Furnace implements Listener {

    private static final HashMap<Integer, FurnaceUpgrade> currentFurnace = new HashMap<>();
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

    public static Inventory inventory(Player player, BackPack backPack, int furnaceId){
        Inventory inventory = Bukkit.createInventory(player, InventoryType.FURNACE);
        FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) backPack.getUpgradeFromId(furnaceId);
        currentFurnace.put(backPack.getId(), furnaceUpgrade);

        inventory.setItem(1, furnaceUpgrade.getFuel());
        inventory.setItem(0, furnaceUpgrade.getSmelting());
        inventory.setItem(2, furnaceUpgrade.getResult());

        if(!firstSave.contains(furnaceId)){
            firstSave.add(furnaceId);
            updateFurnace(backPack.getId(), player, furnaceUpgrade);
        }

        return inventory;
    }

    private static void updateFurnace(int backpackId, Player player, FurnaceUpgrade upgrade){

        if(Main.backPackManager.getBackpackFromId(backpackId) == null){
            return;
        }
        BukkitTask task = new BukkitRunnable() {
            int operation = 0;
            int lastMaxOperation = 0;
            @Override
            public void run() {
                if(upgrade.getSmelting() == null){
                    operation = 0;
                    return;
                }

                if(upgrade.getFuel() == null)    return;

                Fuel fuel = getFuelFromItem(upgrade.getFuel());
                int maxOperation = getMaxOperationsFromFuel(fuel);

                if(fuel == Fuel.NOTHING){
                    //check to update the lava_bucket fuel status
                    if(lastMaxOperation > 0){
                        if(lastMaxOperation == 100 && upgrade.getFuel().getType().equals(Material.BUCKET)){
                            maxOperation = 100;
                            lastMaxOperation = maxOperation;
                        }   else return;
                    }   else return;
                }

                if(lastMaxOperation > 0){
                    if(lastMaxOperation != maxOperation) operation = 0;
                }

                lastMaxOperation = maxOperation;

                if(upgrade.getResult() != null){
                    if(upgrade.getResult().getAmount() == upgrade.getResult().getMaxStackSize()) return;
                }

                for(FurnaceRecipe recipe : Main.getMain().getFurnaceRecipes()){
                    if(!recipe.getInputChoice().test(upgrade.getSmelting())) continue;

                    if(upgrade.getResult() == null){
                        if(operation == 0){
                            if(maxOperation != 1)   operation = 1;
                            if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                                else    upgrade.setFuel(upgrade.getFuel().subtract());
                            }
                        }
                        else{
                            if(operation >= maxOperation - 1){
                                operation = 0;
                                if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                                else{
                                    if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                                    else    upgrade.setFuel(upgrade.getFuel().subtract());
                                }
                            }   else operation++;
                        }
                        upgrade.setSmelting(upgrade.getSmelting().subtract());
                        upgrade.setResult(recipe.getResult());
                        updateOpenInv(player);
                        return;
                    }

                    if(!upgrade.getResult().isSimilar(recipe.getResult())) return;

                    if(operation == 0){
                        if(maxOperation != 1)    operation = 1;
                        if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else{
                            if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else    upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }
                    else{
                        if(operation >= maxOperation - 1){
                            operation = 0;
                            if(maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                            else{
                                if(upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                                else    upgrade.setFuel(upgrade.getFuel().subtract());
                            }
                        }   else operation++;
                    }

                    upgrade.setSmelting(upgrade.getSmelting().subtract());
                    upgrade.setResult(upgrade.getResult().add());
                    updateOpenInv(player);
                    return;
                }
            }
        }.runTaskTimer(Main.getMain(), 0L, 200L);

       taskMap.put(upgrade.getId(), task);

    }

    private static void updateOpenInv(Player player){
        if(!BackpackAction.getAction(player).equals(BackpackAction.Action.UPGFURNACE)) return;
        FurnaceUpgrade upgrade = currentFurnace.get(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));

        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.setItem(1, upgrade.getFuel());
        inventory.setItem(0, upgrade.getSmelting());
        inventory.setItem(2, upgrade.getResult());
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGFURNACE)) return;
        int id = Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                currentFurnace.get(id).setFuel(event.getInventory().getItem(1));
                currentFurnace.get(id).setSmelting(event.getInventory().getItem(0));
                currentFurnace.get(id).setResult(event.getInventory().getItem(2));
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        int id = backPack.getId();

        if(!firstSave.contains(currentFurnace.get(id).getId())){
            firstSave.add(currentFurnace.get(id).getId());
        }
        currentFurnace.get(id).setFuel(event.getInventory().getItem(1));
        currentFurnace.get(id).setSmelting(event.getInventory().getItem(0));
        currentFurnace.get(id).setResult(event.getInventory().getItem(2));

        if(currentFurnace.get(id).getFuel() == null || currentFurnace.get(id).getSmelting() == null){
            taskMap.get(currentFurnace.get(id).getId()).cancel();
            taskMap.remove(currentFurnace.get(id).getId());
            firstSave.remove(currentFurnace.get(id).getId());
            currentFurnace.remove(backPack.getId());
        }

        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);
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
