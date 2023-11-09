package br.com.Backpacks.recipes;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Recipes {

    public NamespacedKey getIS_BACKPACK() {
        return IS_BACKPACK;
    }

    private final NamespacedKey IS_BACKPACK = new NamespacedKey("backpacks", "isbackpack");
    private final NamespacedKey LEATHER_BACKPACK = new NamespacedKey("backpacks", "leatherbackpack");

    public void create_test_backpack(Player player){
        ItemStack item = new ItemStack(org.bukkit.Material.CHEST);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Teste");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);

        meta.getPersistentDataContainer().set(IS_BACKPACK, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(LEATHER_BACKPACK, PersistentDataType.INTEGER, 1);

        item.setItemMeta(meta);

        player.getInventory().addItem(item);
    }

}
