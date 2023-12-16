package br.com.backpacks.events.upgrades_related;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FurnaceGrid implements Listener {

    private static final Set<UUID> firstSave = new HashSet<>();

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, InventoryType.FURNACE);

        inventory.setItem(1, backPack.getFuel());
        inventory.setItem(0, backPack.getSmelting());
        inventory.setItem(2, backPack.getResult());

        if(!firstSave.contains(player.getUniqueId())){
            Main.getMain().getLogger().info("first time");
            updateFurnace(backPack, player);
        }

        return inventory;
    }
    private static void updateFurnace(BackPack backPack, Player player){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(backPack.getSmelting() == null) return;
                if(backPack.getFuel() == null) return;

                if(backPack.getResult() != null){
                    if(backPack.getResult().getAmount() == backPack.getResult().getMaxStackSize()) return;
                }

                for(FurnaceRecipe recipe : Main.getMain().getFurnaceRecipes()){
                    if(!recipe.getInputChoice().test(backPack.getSmelting())) continue;

                    if(backPack.getResult() == null){
                        backPack.setSmelting(backPack.getSmelting().subtract());
                        backPack.setResult(recipe.getResult());
                        checkOpenInv(backPack, player);
                        Main.getMain().getLogger().info(String.valueOf(backPack.getResult().getAmount()));
                        return;
                    }

                    if(!backPack.getResult().isSimilar(recipe.getResult())) return;

                    backPack.setSmelting(backPack.getSmelting().subtract());
                    backPack.setResult(backPack.getResult().add());
                    checkOpenInv(backPack, player);
                    Main.getMain().getLogger().info(String.valueOf(backPack.getResult().getAmount()));
                    return;
                }
            }
        }.runTaskTimer(Main.getMain(), 1L, 200L);
    }

    private static void checkOpenInv(BackPack backPack, Player player){
        if(BackpackAction.getAction(player) == BackpackAction.Action.UPGFURNACE){
            player.getOpenInventory().getTopInventory().setItem(2, backPack.getResult());
            player.getOpenInventory().getTopInventory().setItem(1, backPack.getFuel());
            player.getOpenInventory().getTopInventory().setItem(0, backPack.getSmelting());
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));


        if(!firstSave.contains(event.getPlayer().getUniqueId())){
            firstSave.add(event.getPlayer().getUniqueId());
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
}
