package br.com.backpacks.utils.menu.backpacksMenus;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.menu.Button;
import br.com.backpacks.utils.menu.DynamicMenu;
import br.com.backpacks.utils.menu.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class UpgradesMenu extends DynamicMenu {
    private final BackPack backpack;

    public UpgradesMenu(int size, String title, BackPack backPack) {
        super(size, title);
        this.backpack = backPack;

        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for(int i = backPack.getType().getMaxUpgrades(); i < inventory.getSize(); i++){
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
    public void onClose(Player player, BackPack backPack) {
        List<Upgrade> newUpgrades = new ArrayList<>();
        List<Integer> newUpgradesIds = new ArrayList<>();

        for(int i = 0; i < backPack.getType().getMaxUpgrades(); i++){
            ItemStack item = inventory.getItem(i);

            if(item == null){
                continue;
            }

            if(RecipesUtils.isItemUpgrade(item)){
                Upgrade upgrade = RecipesUtils.getUpgradeFromItem(item);

                if(!UpgradeManager.canUpgradeStack(upgrade)){
                    boolean shouldSkip = false;

                    for(Upgrade upgrade1 : newUpgrades){
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

                newUpgrades.add(upgrade);
                newUpgradesIds.add(upgrade.getId());
            }

            else{
                inventory.remove(item);
                player.getInventory().addItem(item);
            }
        }

        //stop ticking upgrades when not in the backpack
        if(!backPack.getBackpackUpgrades().isEmpty()){
            if(newUpgrades.isEmpty()){
                backPack.stopTickingAllUpgrades();
            }

            else{
                for(Upgrade upgrade : backPack.getBackpackUpgrades()){
                    //was removed
                    if(!newUpgradesIds.contains(upgrade.getId())){
                        upgrade.stopTicking();
                    }
                }
            }
        }

        backPack.setBackpackUpgrades(newUpgrades);
        backPack.getConfigMenu().addUpgradesInView();

        backpack.getUpgradesInputOutputMenu().getEditInputOutputMenu().refreshMenu();
        BackpackAction.clearPlayerAction(player);
         BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open(player);
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    @Override
    public void onClickBottomInventory(Player player, BackPack backPack, InventoryClickEvent event) {
        super.onClickBottomInventory(player, backPack, event);
        event.setCancelled(false);
    }
}
