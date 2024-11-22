package br.com.backpacks.utils.menu.backpacksMenus;

import br.com.backpacks.Main;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.menu.Button;
import br.com.backpacks.utils.menu.ItemCreator;
import br.com.backpacks.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UpgradesInputOutputMenu extends Menu {
    private final UpgradesEditInputOutputMenu editInputOutputMenu;

    public UpgradesInputOutputMenu(BackPack backPack) {
        super(27, "Input/Output Menu", backPack);

        this.editInputOutputMenu = new UpgradesEditInputOutputMenu(9, "Edit", backPack);

        ItemStack resetDefault = new ItemCreator(Material.EMERALD_ORE, "Reset Input/Output configuration").build();
        ItemStack input = new ItemCreator(Material.HOPPER, "Set new input inventory").build();
        ItemStack output = new ItemCreator(Material.DISPENSER, "Set new output inventory").build();

        addButton(new Button(11) {
            @Override
            public ItemStack getItem() {
                return input;
            }

            @Override
            public void onClick(Player player) {
                BackpackAction.clearPlayerAction(player);
                editInputOutputMenu.displayTo(player);
                player.getOpenInventory().setTitle("Edit Input");
                BackpackAction.setAction(player, BackpackAction.Action.EDITINPUT);
            }
        });

        addButton(new Button(13) {
            @Override
            public ItemStack getItem() {
                return resetDefault;
            }

            @Override
            public void onClick(Player player) {
                backPack.setInputUpgrade(-1);
                backPack.setOutputUpgrade(-1);
                player.closeInventory();
            }
        });

        addButton(new Button(15) {
            @Override
            public ItemStack getItem() {
                return output;
            }

            @Override
            public void onClick(Player player) {
                BackpackAction.clearPlayerAction(player);
                editInputOutputMenu.displayTo(player);
                player.getOpenInventory().setTitle("Edit Output");
                BackpackAction.setAction(player, BackpackAction.Action.EDITOUTPUT);
            }
        });

    }

    @Override
    public void onClose(Player player) {
        BackpackAction.clearPlayerAction(player);

        Bukkit.getScheduler().runTaskLater(Main.getMain(), () ->{
            backPack.open(player);
        }, 1L);
    }

    @Override
    public void onClickBottomInventory(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
    }

    public UpgradesEditInputOutputMenu getEditInputOutputMenu(){
        return this.editInputOutputMenu;
    }

}
