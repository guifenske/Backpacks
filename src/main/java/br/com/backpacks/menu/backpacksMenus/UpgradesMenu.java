package br.com.backpacks.menu.backpacksMenus;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import br.com.backpacks.menu.Button;
import br.com.backpacks.menu.ItemCreator;
import br.com.backpacks.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class UpgradesMenu extends Menu {
    public UpgradesMenu(Backpack backpack) {
        super(9, "Upgrades Menu", backpack);

        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for(int i = backpack.getType().getMaxUpgrades(); i < inventory.getSize(); i++){
            addButton(new Button(i) {
                @Override
                public ItemStack getItem() {
                    return blank;
                }

                @Override
                public void onClick(Player player) {
                }
            });
        }

        if(!backpack.getBackpackUpgrades().isEmpty()) {
            List<Upgrade> upgrades = backpack.getBackpackUpgrades();

            int i = 0;
            for(Upgrade upgrade : upgrades) {
                inventory.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                i++;
            }
        }

    }

    @Override
    public void onClose(Player player) {
        List<Integer> newUpgradesIds = new ArrayList<>();

        for(int i = 0; i < backpack.getType().getMaxUpgrades(); i++){
            ItemStack item = inventory.getItem(i);

            if(item == null){
                continue;
            }

            if(RecipesUtils.isItemUpgrade(item)){
                Upgrade upgrade = RecipesUtils.getUpgradeFromItem(item);

                if(!UpgradeManager.canUpgradeStack(upgrade)){
                    boolean shouldSkip = false;

                    for(int id : newUpgradesIds){
                        Upgrade upgrade1 = UpgradeManager.getUpgradeFromId(id);

                        if(upgrade1.getType() == upgrade.getType()){
                            inventory.remove(item);
                            player.getInventory().addItem(item);
                            shouldSkip = true;
                            break;
                        }
                    }

                    if(shouldSkip) continue;
                }

                if(item.getAmount() > 1){
                    ItemStack leftOvers = item.clone();
                    leftOvers.setAmount(item.getAmount() - 1);

                    ItemStack playerItems = item.clone();
                    playerItems.setAmount(1);

                    player.getInventory().addItem(leftOvers);
                    inventory.setItem(i, playerItems);
                }

                newUpgradesIds.add(upgrade.getId());
            }

            else{
                inventory.remove(item);
                player.getInventory().addItem(item);
            }
        }

        //stop ticking upgrades when not in the backpack
        if(!backpack.getBackpackUpgrades().isEmpty()){
            if(newUpgradesIds.isEmpty()){
                backpack.stopTickingAllUpgrades();
            }

            else{
                for(Upgrade upgrade : backpack.getBackpackUpgrades()){
                    //was removed
                    if(!newUpgradesIds.contains(upgrade.getId())){
                        upgrade.stopTicking();
                    }
                }
            }
        }

        backpack.setUpgradesIds(newUpgradesIds);
        backpack.getConfigMenu().addUpgradesInView();

        backpack.getUpgradesInputOutputMenu().getEditInputOutputMenu().refreshMenu();

        BackpackAction.clearPlayerAction(player);
         BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backpack.open(player);
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @Override
    public void onClickBottomInventory(Player player, InventoryClickEvent event) {
        event.setCancelled(false);
    }
}
