package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.UpgradeType;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Furnace implements Listener {
    public static final ConcurrentHashMap<UUID, FurnaceUpgrade> currentFurnace = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, BukkitTask> taskMap = new ConcurrentHashMap<>();
    public static final IntOpenHashSet shouldTick = new IntOpenHashSet();
    private static final EnumMap<Material, Fuel> fuelMap = new EnumMap<>(Material.class);
    private static final EnumMap<Fuel, Integer> maxOperationsMap = new EnumMap<>(Fuel.class);
    private static final HashMap<Material, Float> expPointsPerMaterial = new HashMap<>();

    static {
        fuelMap.put(Material.BLACK_CARPET, Fuel.CARPET);
        fuelMap.put(Material.BLUE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.BROWN_CARPET, Fuel.CARPET);
        fuelMap.put(Material.CYAN_CARPET, Fuel.CARPET);
        fuelMap.put(Material.GRAY_CARPET, Fuel.CARPET);
        fuelMap.put(Material.GREEN_CARPET, Fuel.CARPET);
        fuelMap.put(Material.LIGHT_BLUE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.LIGHT_GRAY_CARPET, Fuel.CARPET);
        fuelMap.put(Material.LIME_CARPET, Fuel.CARPET);
        fuelMap.put(Material.MAGENTA_CARPET, Fuel.CARPET);
        fuelMap.put(Material.ORANGE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.PINK_CARPET, Fuel.CARPET);
        fuelMap.put(Material.PURPLE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.RED_CARPET, Fuel.CARPET);
        fuelMap.put(Material.WHITE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.YELLOW_CARPET, Fuel.CARPET);
        fuelMap.put(Material.DRIED_KELP_BLOCK, Fuel.DRIED_KELP_BLOCK);
        fuelMap.put(Material.LAVA_BUCKET, Fuel.LAVA_BUCKET);
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
        maxOperationsMap.put(Fuel.CARPET, 1);

        expPointsPerMaterial.put(Material.DRIED_KELP, 0.1f);
        expPointsPerMaterial.put(Material.COAL, 0.1f);
        expPointsPerMaterial.put(Material.GREEN_DYE, 0.1f);
        expPointsPerMaterial.put(Material.STONE, 0.1f);
        expPointsPerMaterial.put(Material.SMOOTH_STONE, 0.1f);
        expPointsPerMaterial.put(Material.CRACKED_STONE_BRICKS, 0.1f);
        expPointsPerMaterial.put(Material.DEEPSLATE, 0.1f);
        expPointsPerMaterial.put(Material.CRACKED_DEEPSLATE_BRICKS, 0.1f);
        expPointsPerMaterial.put(Material.CRACKED_DEEPSLATE_TILES, 0.1f);
        expPointsPerMaterial.put(Material.SMOOTH_SANDSTONE, 0.1f);
        expPointsPerMaterial.put(Material.SMOOTH_RED_SANDSTONE, 0.1f);
        expPointsPerMaterial.put(Material.CRACKED_NETHER_BRICKS, 0.1f);
        expPointsPerMaterial.put(Material.SMOOTH_BASALT, 0.1f);
        expPointsPerMaterial.put(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.1f);
        expPointsPerMaterial.put(Material.SMOOTH_QUARTZ, 0.1f);
        expPointsPerMaterial.put(Material.GRAY_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.GREEN_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.BLACK_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.BLUE_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.BROWN_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.CYAN_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.LIME_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.YELLOW_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.WHITE_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.ORANGE_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.MAGENTA_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.PINK_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.PURPLE_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.RED_GLAZED_TERRACOTTA, 0.1f);
        expPointsPerMaterial.put(Material.POPPED_CHORUS_FRUIT, 0.1f);
        expPointsPerMaterial.put(Material.LIME_DYE, 0.1f);
        expPointsPerMaterial.put(Material.NETHER_BRICK, 0.1f);
        expPointsPerMaterial.put(Material.IRON_NUGGET, 0.1f);
        expPointsPerMaterial.put(Material.GLASS, 0.1f);
        expPointsPerMaterial.put(Material.GOLD_NUGGET, 0.1f);
        expPointsPerMaterial.put(Material.SPONGE, 0.15f);
        expPointsPerMaterial.put(Material.CHARCOAL, 0.15f);
        expPointsPerMaterial.put(Material.LAPIS_LAZULI, 0.2f);
        expPointsPerMaterial.put(Material.REDSTONE, 0.3f);
        expPointsPerMaterial.put(Material.BRICK, 0.35f);
        expPointsPerMaterial.put(Material.TERRACOTTA, 0.35f);
        expPointsPerMaterial.put(Material.BAKED_POTATO, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_BEEF, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_PORKCHOP, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_CHICKEN, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_MUTTON, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_COD, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_RABBIT, 0.35f);
        expPointsPerMaterial.put(Material.COOKED_SALMON, 0.35f);
        expPointsPerMaterial.put(Material.IRON_INGOT, 0.7f);
        expPointsPerMaterial.put(Material.COPPER_INGOT, 0.7f);
        expPointsPerMaterial.put(Material.GOLD_INGOT, 1.0f);
        expPointsPerMaterial.put(Material.EMERALD, 1.0f);
        expPointsPerMaterial.put(Material.DIAMOND, 1.0f);
        expPointsPerMaterial.put(Material.NETHERITE_SCRAP, 2.0f);
    }

    public static void tick(FurnaceUpgrade upgrade){
        if(upgrade == null){
            return;
        }
        upgrade.startSubTick();

        Main.getMain().getThreadBackpacks().getExecutor().submit(() ->{
            if(upgrade.getBoundFakeBlock() == null) {
                Location tempLocation = new Location(Bukkit.getWorld("world"), ThreadLocalRandom.current().nextInt(-900, 900), -65, ThreadLocalRandom.current().nextInt(-900, 900));
                Block tempBlock = tempLocation.getBlock();
                switch (upgrade.getType()) {
                    case FURNACE -> tempBlock.setType(Material.FURNACE);
                    case SMOKER -> tempBlock.setType(Material.SMOKER);
                    case BLAST_FURNACE -> tempBlock.setType(Material.BLAST_FURNACE);
                }
                upgrade.setBoundFakeBlock(tempBlock);
            }
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if(!upgrade.canTick()){
                        upgrade.clearSubTickTask();
                        upgrade.setCookTime(0);
                        this.cancel();
                        upgrade.setBoundFakeBlock(null);
                        Main.getMain().debugMessage("Stopping furnace task for " + upgrade.getId() + "!");
                        return;
                    }

                    Fuel fuel = getFuelFromItem(upgrade.getFuel());
                    int maxOperation = getMaxOperationsFromFuel(fuel);
                    if(fuel.equals(Fuel.NOTHING)){
                        if(upgrade.getLastMaxOperation() > 0 && upgrade.getOperation() > 0){
                            if(upgrade.getLastMaxOperation() == 100 && upgrade.getFuel().getType().equals(Material.BUCKET)){
                                fuel = Fuel.LAVA_BUCKET;
                                maxOperation = 100;
                                upgrade.setLastMaxOperation(maxOperation);
                            }   else    maxOperation = upgrade.getLastMaxOperation();
                        }   else   return;
                    }

                    if(upgrade.getFuel() != null && fuel.equals(Fuel.NOTHING)){
                        this.cancel();
                        upgrade.setCookTime(0);
                        upgrade.getSubTickTask().cancel();
                        upgrade.setBoundFakeBlock(null);
                        Main.getMain().getLogger().severe("Furnace " + upgrade.getId() + " has a invalid fuel! Fuel = " + upgrade.getFuel() + ", if this is a bug or a valid fuel, report to the developer!");
                        return;
                    }

                    if(upgrade.getLastMaxOperation() > 0){
                        if(upgrade.getLastMaxOperation() != maxOperation) upgrade.setOperation(0);
                    }

                    upgrade.setLastMaxOperation(maxOperation);

                    if(upgrade.getType().equals(UpgradeType.SMOKER)){
                        smokerLogic(upgrade, maxOperation);
                    }   else if(upgrade.getType().equals(UpgradeType.BLAST_FURNACE)){
                        blastFurnaceLogic(upgrade, maxOperation);
                    }   else {
                        furnaceLogic(upgrade, maxOperation);
                    }
                }
            }.runTaskTimer(Main.getMain(), upgrade.getCookItemTicks(), upgrade.getCookItemTicks());

            taskMap.put(upgrade.getId(), task);
        });

       Main.getMain().debugMessage("Starting furnace task for " + upgrade.getId() + "!");
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGFURNACE)) return;
        Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() == 2){
            if(event.getCurrentItem() != null && currentFurnace.get(player.getUniqueId()).getResult().isSimilar(event.getCurrentItem())){
                if(event.getCurrentItem().getAmount() * expPointsPerMaterial.getOrDefault(event.getCurrentItem().getType(), 0f) > 1){
                    player.giveExp((int) (event.getCurrentItem().getAmount() * expPointsPerMaterial.getOrDefault(event.getCurrentItem().getType(), 0f)));
                }   else{
                    player.setExp(event.getCurrentItem().getAmount() * expPointsPerMaterial.getOrDefault(event.getCurrentItem().getType(), 0f));
                }
            }
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                currentFurnace.get(player.getUniqueId()).setFuel(event.getInventory().getItem(1));
                currentFurnace.get(player.getUniqueId()).setSmelting(event.getInventory().getItem(0));
                currentFurnace.get(player.getUniqueId()).setResult(event.getInventory().getItem(2));
                if(!shouldTick.contains(currentFurnace.get(player.getUniqueId()).getId())) {
                    if (currentFurnace.get(player.getUniqueId()).canTick()) {
                        shouldTick.add(currentFurnace.get(player.getUniqueId()).getId());
                        tick(currentFurnace.get(player.getUniqueId()));
                    }
                }   else{
                    if(!currentFurnace.get(player.getUniqueId()).canTick()){
                        taskMap.get(currentFurnace.get(player.getUniqueId()).getId()).cancel();
                        currentFurnace.get(player.getUniqueId()).setCookTime(0);
                        player.getOpenInventory().setProperty(InventoryView.Property.COOK_TIME, 0);
                        currentFurnace.get(player.getUniqueId()).clearSubTickTask();
                        if(currentFurnace.get(player.getUniqueId()).getBoundFakeBlock() != null) {
                            currentFurnace.get(player.getUniqueId()).setBoundFakeBlock(null);
                        }
                        shouldTick.remove(currentFurnace.get(player.getUniqueId()).getId());
                    }
                }
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        Player player = (Player) event.getPlayer();

        currentFurnace.get(player.getUniqueId()).setFuel(event.getInventory().getItem(1));
        currentFurnace.get(player.getUniqueId()).setSmelting(event.getInventory().getItem(0));
        currentFurnace.get(player.getUniqueId()).setResult(event.getInventory().getItem(2));

        if(!currentFurnace.get(player.getUniqueId()).canTick()){
            if(taskMap.containsKey(currentFurnace.get(player.getUniqueId()).getId())) {
                currentFurnace.get(player.getUniqueId()).setCookTime(0);
                taskMap.get(currentFurnace.get(player.getUniqueId()).getId()).cancel();
                taskMap.remove(currentFurnace.get(player.getUniqueId()).getId());
            }
            if(currentFurnace.get(player.getUniqueId()).getBoundFakeBlock() != null) {
                currentFurnace.get(player.getUniqueId()).setBoundFakeBlock(null);
            }
            currentFurnace.get(player.getUniqueId()).clearSubTickTask();
            shouldTick.remove(currentFurnace.get(player.getUniqueId()).getId());
            currentFurnace.remove(player.getUniqueId());
        }

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
        CARPET,
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
            ItemStack result;
            if(upgrade.getResult() != null) result = upgrade.getResult().clone();
            else    result = recipe.getResult();

            FurnaceSmeltEvent e = new FurnaceSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), result, recipe);
            Bukkit.getPluginManager().callEvent(e);

            if(!e.isCancelled()) {
                if(upgrade.getResult() != null && !upgrade.getResult().isSimilar(e.getResult())) return;
                if(upgrade.getOperation() == 0 || upgrade.getOperation() >= maxOperation - 1){
                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(upgrade.getBoundFakeBlock(), upgrade.getFuel(), upgrade.getCookTime());
                    Bukkit.getPluginManager().callEvent(furnaceBurnEvent);
                    if(!furnaceBurnEvent.isCancelled()) {
                        if (upgrade.getFuel() == null) upgrade.setOperation(0);
                        else upgrade.setOperation(1);
                        if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else {
                            if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }   else{
                        upgrade.setFuel(furnaceBurnEvent.getFuel());
                    }
                } else upgrade.setOperation(upgrade.getOperation() + 1);

                if(upgrade.getResult() != null) upgrade.setResult(e.getResult().add());
                else   upgrade.setResult(e.getResult());
                if(e.getSource().getAmount() == 1){
                    taskMap.get(upgrade.getId()).cancel();
                    upgrade.getSubTickTask().cancel();
                }
                upgrade.setSmelting(e.getSource().subtract());
                upgrade.setCookTime(0);
                upgrade.updateInventory();
            }   else{
                upgrade.setSmelting(e.getSource());
                upgrade.setResult(e.getResult());
            }

            FurnaceStartSmeltEvent e2 = new FurnaceStartSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), recipe, recipe.getCookingTime());
            Bukkit.getPluginManager().callEvent(e2);
            return;
        }
    }

    private static void smokerLogic(FurnaceUpgrade upgrade, int maxOperation){
        for(SmokingRecipe recipe : Main.getMain().getSmokingRecipes()){
            if(!recipe.getInputChoice().test(upgrade.getSmelting())) continue;
            ItemStack result;
            if(upgrade.getResult() != null) result = upgrade.getResult().clone();
            else    result = recipe.getResult();

            FurnaceSmeltEvent e = new FurnaceSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), result, recipe);
            Bukkit.getPluginManager().callEvent(e);

            if(!e.isCancelled()) {
                if(upgrade.getResult() != null && !upgrade.getResult().isSimilar(e.getResult())) return;
                if(upgrade.getOperation() == 0 || upgrade.getOperation() >= maxOperation - 1){
                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(upgrade.getBoundFakeBlock(), upgrade.getFuel(), upgrade.getCookTime());
                    Bukkit.getPluginManager().callEvent(furnaceBurnEvent);
                    if(!furnaceBurnEvent.isCancelled()) {
                        if (upgrade.getFuel() == null) upgrade.setOperation(0);
                        else upgrade.setOperation(1);
                        if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else {
                            if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }   else{
                        upgrade.setFuel(furnaceBurnEvent.getFuel());
                    }
                } else upgrade.setOperation(upgrade.getOperation() + 1);

                if(upgrade.getResult() != null) upgrade.setResult(e.getResult().add());
                else   upgrade.setResult(e.getResult());
                if(e.getSource().getAmount() == 1){
                    taskMap.get(upgrade.getId()).cancel();
                    upgrade.getSubTickTask().cancel();
                }
                upgrade.setSmelting(e.getSource().subtract());
                upgrade.setCookTime(0);
                upgrade.updateInventory();
            }   else{
                upgrade.setSmelting(e.getSource());
                upgrade.setResult(e.getResult());
            }

            FurnaceStartSmeltEvent e2 = new FurnaceStartSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), recipe, recipe.getCookingTime());
            Bukkit.getPluginManager().callEvent(e2);
            return;
        }
    }
    private static void blastFurnaceLogic(FurnaceUpgrade upgrade, int maxOperation){
        for(BlastingRecipe recipe : Main.getMain().getBlastingRecipes()){
            if(!recipe.getInputChoice().test(upgrade.getSmelting())) continue;
            ItemStack result;
            if(upgrade.getResult() != null) result = upgrade.getResult().clone();
            else    result = recipe.getResult();

            FurnaceSmeltEvent e = new FurnaceSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), result, recipe);
            Bukkit.getPluginManager().callEvent(e);

            if(!e.isCancelled()) {
                if(upgrade.getResult() != null && !upgrade.getResult().isSimilar(e.getResult())) return;
                if(upgrade.getOperation() == 0 || upgrade.getOperation() >= maxOperation - 1){
                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(upgrade.getBoundFakeBlock(), upgrade.getFuel(), upgrade.getCookTime());
                    Bukkit.getPluginManager().callEvent(furnaceBurnEvent);
                    if(!furnaceBurnEvent.isCancelled()) {
                        if (upgrade.getFuel() == null) upgrade.setOperation(0);
                        else upgrade.setOperation(1);
                        if (maxOperation == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
                        else {
                            if (upgrade.getFuel() == null || upgrade.getFuel().getAmount() == 1) upgrade.setFuel(null);
                            else upgrade.setFuel(upgrade.getFuel().subtract());
                        }
                    }   else{
                        upgrade.setFuel(furnaceBurnEvent.getFuel());
                    }
                } else upgrade.setOperation(upgrade.getOperation() + 1);

                if(upgrade.getResult() != null) upgrade.setResult(e.getResult().add());
                else   upgrade.setResult(e.getResult());
                if(e.getSource().getAmount() == 1){
                    taskMap.get(upgrade.getId()).cancel();
                    upgrade.getSubTickTask().cancel();
                }
                upgrade.setSmelting(e.getSource().subtract());
                upgrade.setCookTime(0);
                upgrade.updateInventory();
            }   else{
                upgrade.setSmelting(e.getSource());
                upgrade.setResult(e.getResult());
            }

            FurnaceStartSmeltEvent e2 = new FurnaceStartSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), recipe, recipe.getCookingTime());
            Bukkit.getPluginManager().callEvent(e2);
            return;
        }
    }
}
