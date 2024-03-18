package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.others.FurnaceUtils;
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

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Furnace implements Listener {
    public static final ConcurrentHashMap<UUID, FurnaceUpgrade> currentFurnace = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, BukkitTask> taskMap = new ConcurrentHashMap<>();
    public static final IntOpenHashSet shouldTick = new IntOpenHashSet();

    public static void tick(FurnaceUpgrade upgrade){
        if(upgrade == null){
            return;
        }
        upgrade.startSubTick();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), ()->{
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
                        this.cancel();
                        upgrade.stopTickingUpgrade();
                        upgrade.setRecipe(null);
                        Main.debugMessage("Stopping furnace task for " + upgrade.getId() + "!");
                        return;
                    }

                    FurnaceUtils.Fuel fuel = getFuelFromItem(upgrade.getFuel());
                    int maxOperation = fuel.getMaxOperation();

                    if(fuel.equals(FurnaceUtils.Fuel.NOTHING)){
                        if(upgrade.getLastMaxOperation() > 0 && upgrade.getOperation() > 0){
                            if(upgrade.getLastMaxOperation() == 100 && upgrade.getFuel().getType().equals(Material.BUCKET)){
                                fuel = FurnaceUtils.Fuel.LAVA_BUCKET;
                                maxOperation = 100;
                                upgrade.setLastMaxOperation(maxOperation);
                            }   else    maxOperation = upgrade.getLastMaxOperation();
                        }   else   return;
                    }

                    if(upgrade.getFuel() != null && fuel.equals(FurnaceUtils.Fuel.NOTHING)){
                        this.cancel();
                        upgrade.stopTickingUpgrade();
                        upgrade.setRecipe(null);
                        Main.getMain().getLogger().severe("Furnace " + upgrade.getId() + " has a invalid fuel! Fuel = " + upgrade.getFuel() + ", if this is a bug or a valid fuel, report to the developer!");
                        return;
                    }

                    if(upgrade.getLastMaxOperation() > 0){
                        if(upgrade.getLastMaxOperation() != maxOperation) upgrade.setOperation(0);
                    }

                    upgrade.setLastMaxOperation(maxOperation);
                    generalLogic(upgrade);
                }
            }.runTaskTimer(Main.getMain(), upgrade.getCookItemTicks(), upgrade.getCookItemTicks());
            taskMap.put(upgrade.getId(), task);
        });
        Main.debugMessage("Starting furnace task for " + upgrade.getId() + "!");
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGFURNACE)) return;
        Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() == 2){
            if(event.getCurrentItem() != null && event.getCurrentItem().isSimilar(currentFurnace.get(player.getUniqueId()).getResult())){
                float expPoints = currentFurnace.get(player.getUniqueId()).getRecipe().getExperience();
                player.giveExp((int) (expPoints * event.getCurrentItem().getAmount()));
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
                        currentFurnace.get(player.getUniqueId()).stopTickingUpgrade();
                    }
                }
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        Player player = (Player) event.getPlayer();

        currentFurnace.get(player.getUniqueId()).setFuel(event.getInventory().getItem(1));
        currentFurnace.get(player.getUniqueId()).setSmelting(event.getInventory().getItem(0));
        currentFurnace.get(player.getUniqueId()).setResult(event.getInventory().getItem(2));

        if(!currentFurnace.get(player.getUniqueId()).canTick()){
            currentFurnace.get(player.getUniqueId()).stopTickingUpgrade();
        }

        BackpackAction.clearPlayerAction(player);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);

    }

    private static FurnaceUtils.Fuel getFuelFromItem(ItemStack item){
        if(item == null) return FurnaceUtils.Fuel.NOTHING;
        return FurnaceUtils.fuelMap.getOrDefault(FurnaceUtils.Material.valueOf(item.getType().name()), FurnaceUtils.Fuel.NOTHING);
    }

    private static void generalLogic(FurnaceUpgrade upgrade){
        CookingRecipe<?> recipe = upgrade.getRecipe();
        if(recipe == null || !recipe.getInputChoice().test(upgrade.getSmelting())){
            switch (upgrade.getType()){
                case FURNACE -> {
                    for(FurnaceRecipe recipe1 : Main.getMain().getFurnaceRecipes()){
                        if(recipe1.getInputChoice().test(upgrade.getSmelting())){
                            upgrade.setRecipe(recipe1);
                            recipe = recipe1;
                            break;
                        }
                    }
                }
                case BLAST_FURNACE -> {
                    for(BlastingRecipe recipe1 : Main.getMain().getBlastingRecipes()){
                        if(recipe1.getInputChoice().test(upgrade.getSmelting())){
                            upgrade.setRecipe(recipe1);
                            recipe = recipe1;
                            break;
                        }
                    }
                }
                case SMOKER -> {
                    for(SmokingRecipe recipe1 : Main.getMain().getSmokingRecipes()){
                        if(recipe1.getInputChoice().test(upgrade.getSmelting())){
                            upgrade.setRecipe(recipe1);
                            recipe = recipe1;
                            break;
                        }
                    }
                }
            }

            if(recipe == null){
                upgrade.stopTickingUpgrade();
                upgrade.setRecipe(null);
                return;
            }
        }

        //the furnaceSmeltEvent needs to use the current itemstack.
        ItemStack result = recipe.getResult();
        if(upgrade.getResult() != null && upgrade.getResult().isSimilar(result)) result = upgrade.getResult();

        FurnaceSmeltEvent e = new FurnaceSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), result, recipe);
        Bukkit.getPluginManager().callEvent(e);

        if(!e.isCancelled()) {
            if(upgrade.getResult() != null && !upgrade.getResult().isSimilar(e.getResult())){
                upgrade.stopTickingUpgrade();
                upgrade.setRecipe(null);
                return;
            }
            if(upgrade.getOperation() == 0 || upgrade.getOperation() >= upgrade.getLastMaxOperation() - 1){
                FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(upgrade.getBoundFakeBlock(), upgrade.getFuel(), upgrade.getCookTime());
                Bukkit.getPluginManager().callEvent(furnaceBurnEvent);
                if(!furnaceBurnEvent.isCancelled()) {
                    if (upgrade.getFuel() == null) upgrade.setOperation(0);
                    else upgrade.setOperation(1);
                    if (upgrade.getLastMaxOperation() == 100) upgrade.setFuel(new ItemStack(Material.BUCKET));
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
                upgrade.stopTickingUpgrade();
                upgrade.setRecipe(null);
            }
            upgrade.setSmelting(e.getSource().subtract());
            upgrade.setCookTime(0);
        }   else{
            upgrade.setSmelting(e.getSource());
            upgrade.setResult(e.getResult());
        }

        upgrade.updateInventory();
        FurnaceStartSmeltEvent e2 = new FurnaceStartSmeltEvent(upgrade.getBoundFakeBlock(), upgrade.getSmelting(), recipe, recipe.getCookingTime());
        Bukkit.getPluginManager().callEvent(e2);
    }
}
