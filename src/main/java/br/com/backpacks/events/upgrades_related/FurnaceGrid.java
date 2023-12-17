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

public class FurnaceGrid implements Listener {

    private static final IntOpenHashSet firstSave = new IntOpenHashSet();

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, InventoryType.FURNACE);

        inventory.setItem(1, backPack.getFuel());
        inventory.setItem(0, backPack.getSmelting());
        inventory.setItem(2, backPack.getResult());

        if(!firstSave.contains(backPack.getId())){
            updateFurnace(backPack.getId(), player);
        }

        return inventory;
    }
    private static void updateFurnace(int id, Player player){
        BukkitTask task = new BukkitRunnable() {
            int operation = 0;
            int lastMaxOperation = 0;
            @Override
            public void run() {
                BackPack backPack = Main.backPackManager.getBackpackFromId(id);
                if(backPack == null){
                    cancel();
                    return;
                }

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
        }.runTaskTimer(Main.getMain(), 1L, 200L);
    }

    private static void updateOpenInv(BackPack backPack, Player player){
        if(BackpackAction.getAction(player) == BackpackAction.Action.UPGFURNACE){
            player.getOpenInventory().getTopInventory().setItem(2, backPack.getResult());
            player.getOpenInventory().getTopInventory().setItem(1, backPack.getFuel());
            player.getOpenInventory().getTopInventory().setItem(0, backPack.getSmelting());
        }
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

        switch (item.getType()) {
            case COAL -> {
                return Fuel.COAL;
            }

            case COAL_BLOCK -> {
                return Fuel.COAL_BLOCK;
            }

            case BLAZE_ROD -> {
                return Fuel.BLAZE_ROD;
            }

            case ACACIA_PLANKS, BAMBOO_PLANKS, BIRCH_PLANKS, CHERRY_PLANKS, CRIMSON_PLANKS, OAK_PLANKS, DARK_OAK_PLANKS, JUNGLE_PLANKS, MANGROVE_PLANKS, SPRUCE_PLANKS, WARPED_PLANKS -> {
                return Fuel.PLANKS;
            }

            case STICK -> {
                return Fuel.STICK;
            }

            case ACACIA_BOAT, ACACIA_CHEST_BOAT, BIRCH_BOAT, BIRCH_CHEST_BOAT, CHERRY_BOAT, CHERRY_CHEST_BOAT, DARK_OAK_BOAT, DARK_OAK_CHEST_BOAT, JUNGLE_BOAT, JUNGLE_CHEST_BOAT, MANGROVE_BOAT, MANGROVE_CHEST_BOAT, OAK_BOAT, OAK_CHEST_BOAT, SPRUCE_BOAT,  SPRUCE_CHEST_BOAT -> {
                return Fuel.BOAT;
            }

            case CHARCOAL -> {
                return Fuel.CHARCOAL;
            }

            case LAVA_BUCKET -> {
                return Fuel.LAVA_BUCKET;
            }

            case DRIED_KELP_BLOCK -> {
                return Fuel.DRIED_KELP_BLOCK;
            }

            case WOODEN_SWORD, WOODEN_AXE, WOODEN_PICKAXE, WOODEN_HOE, WOODEN_SHOVEL -> {
                return Fuel.WOODEN_EQUIPMENT;
            }

            case BOWL -> {
                return Fuel.BOWL;
            }
        }

        return Fuel.NOTHING;

    }

    private static int getMaxOperationsFromFuel(Fuel fuel){
        switch (fuel){
            case COAL, CHARCOAL -> {
                return 8;
            }

            case COAL_BLOCK -> {
                return 80;
            }

            case LAVA_BUCKET -> {
                return 100;
            }

            case DRIED_KELP_BLOCK -> {
                return 20;
            }

            case STICK, BOWL, WOODEN_EQUIPMENT, PLANKS -> {
                return 1;
            }

            case BLAZE_ROD -> {
                return 12;
            }

            case BOAT -> {
                return 6;
            }
        }

        return 0;
    }
}
