package br.com.backpacks.utils.menu;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UpgradesEditInputOutputMenu extends DynamicMenu {
    private final BackPack backPack;

    public UpgradesEditInputOutputMenu(int size, String title, BackPack backPack) {
        super(size, title);

        this.backPack = backPack;
        refreshMenu();
    }

    public void refreshMenu(){
        ItemStack blankItem = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").build();

        for(int i = 0; i < backPack.getType().getMaxUpgrades(); i++){
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

        for(int i = backPack.getType().getMaxUpgrades(); i < 9; i++){
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
        List<Upgrade> upgrades = backPack.getBackpackUpgrades();

        if (backPack.getBackpackUpgrades().isEmpty()) {
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
                        backPack.setOutputUpgrade(upgrade.getId());
                        player.closeInventory();
                        return;
                    }

                    backPack.setInputUpgrade(upgrade.getId());
                    player.closeInventory();
                }
            });

            i++;
        }
    }

    @Override
    public void onClose(Player player, BackPack backPack) {
        BackpackAction.clearPlayerAction(player);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open(player);
        }, 1L);
    }
}
