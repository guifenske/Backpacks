package br.com.backpacks.backpackUtils.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.recipes.RecipesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class UpgradeMenu {

    public static Inventory editUpgrades(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9, "Upgrades Menu");

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));
        ItemStack item = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, " ").get();

        for(int i = InventoryBuilder.getFreeUpgradesSlots(backPack.getType()); i < 9; i++){
            inv.setItem(i, item);
        }

        int i = 0;
        Set<Upgrade> upgrades = backPack.getUpgrades();
        if(backPack.getUpgrades() != null) {
            if (!backPack.getUpgrades().isEmpty()) {
                for (Upgrade upgrade : upgrades) {
                    inv.setItem(i, RecipesUtils.getItemFromUpgrade(upgrade));
                    i++;
                }
            }
        }

        return inv;
    }
}
