package br.com.backpacks.menu.backpacksMenus;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import br.com.backpacks.menu.Button;
import br.com.backpacks.menu.ItemCreator;
import br.com.backpacks.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UpgradesEditInputOutputMenu extends Menu {

    public UpgradesEditInputOutputMenu(int size, String title, Backpack backpack) {
        super(size, title, backpack);
        refreshMenu();
    }

    public void refreshMenu(){
        ItemStack blankItem = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for(int i = 0; i < backpack.getType().getMaxUpgrades(); i++){
            addButton(new Button(i) {
                @Override
                public ItemStack getItem() {
                    return null;
                }

                @Override
                public void onClick(Player player) {

                }
            });
        }

        for(int i = backpack.getType().getMaxUpgrades(); i < 9; i++){
            addButton(new Button(i) {
                @Override
                public ItemStack getItem() {
                    return blankItem;
                }

                @Override
                public void onClick(Player player) {

                }
            });
        }

        int i = 0;
        List<Upgrade> upgrades = backpack.getBackpackUpgrades();

        if (backpack.getBackpackUpgrades().isEmpty()) {
            return;
        }

        for (Upgrade upgrade : upgrades) {

            if(!upgrade.isAdvanced()) continue;

            addButton(new Button(i) {

                @Override
                public ItemStack getItem() {
                    return RecipesUtils.getItemFromUpgrade(upgrade);
                }

                @Override
                public void onClick(Player player) {
                    if(BackpackAction.getAction(player).equals(BackpackAction.Action.EDITOUTPUT)){
                        backpack.setOutputUpgrade(upgrade.getId());
                        player.closeInventory();
                        return;
                    }

                    backpack.setInputUpgrade(upgrade.getId());
                    player.closeInventory();
                }
            });

            i++;
        }
    }

    @Override
    public void onClose(Player player) {
        BackpackAction.clearPlayerAction(player);

        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backpack.open(player);
        }, 1L);
    }

    @Override
    public void onClickBottomInventory(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
