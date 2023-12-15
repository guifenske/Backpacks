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

public class FurnaceGrid implements Listener {

    public Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, InventoryType.FURNACE);

        inventory.setItem(1, backPack.getFuel());
        inventory.setItem(0, backPack.getSmelting());
        inventory.setItem(2, backPack.getResult());

        updateFurnace(backPack, player);

        return inventory;
    }
    private void updateFurnace(BackPack backPack, Player player){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(backPack.getSmelting() == null) return;

                if(backPack.getResult() != null){
                    if(backPack.getResult().getAmount() == backPack.getResult().getMaxStackSize()) return;
                }

                for(FurnaceRecipe recipe : Main.getMain().getFurnaceRecipes()){
                    if(!recipe.getInputChoice().test(backPack.getSmelting())) continue;
                    backPack.setSmelting(backPack.getSmelting().subtract());

                    if(backPack.getResult() == null){
                        backPack.setResult(recipe.getResult());
                        if(BackpackAction.getAction(player) == BackpackAction.Action.UPGFURNACE){
                            BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
                            player.openInventory(new FurnaceGrid().inventory(player, backPack));
                            BackpackAction.setAction(player, BackpackAction.Action.UPGFURNACE);
                            this.cancel();
                        }
                        break;
                    }

                    if(!backPack.getResult().isSimilar(recipe.getResult())) return;

                    backPack.setResult(backPack.getResult().add());
                    BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
                    player.openInventory(new FurnaceGrid().inventory(player, backPack));
                    BackpackAction.setAction(player, BackpackAction.Action.UPGFURNACE);
                    this.cancel();
                    break;
                }
            }
        }.runTaskTimer(Main.getMain(), 40L, 200L); //run every 200 ticks to mimic the furnace logic
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGFURNACE)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        Player player = (Player) event.getPlayer();

        backPack.setFuel(event.getInventory().getItem(1));
        backPack.setSmelting(event.getInventory().getItem(0));
        backPack.setResult(event.getInventory().getItem(2));

        new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open(player);
            }
        }.runTaskLater(Main.getMain(), 1L);
    }
}
