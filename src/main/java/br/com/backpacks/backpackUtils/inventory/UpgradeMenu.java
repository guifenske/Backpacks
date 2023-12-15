package br.com.backpacks.backpackUtils.inventory;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class UpgradeMenu {

    public static Inventory editUpgrades(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9, "Upgrades Menu");

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(player.getUniqueId()));

        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 10);
        item.setItemMeta(meta);

        for(int i = InventoryBuilder.getFreeInitialSlots(backPack.getType()); i < 9; i++){
            inv.setItem(i, item);
        }

        int i = 0;
        List<Upgrade> upgrades = backPack.getUpgrades();
        if(backPack.getUpgrades() != null) {
            if (!backPack.getUpgrades().isEmpty()) {
                for (Upgrade upgrade : upgrades) {
                    inv.setItem(i, Utils.getItemFromUpgrade(upgrade));
                    i++;
                }
            }
        }

        return inv;
    }
}
