package br.com.backpacks.recipes;

import br.com.backpacks.backpackUtils.BackPack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Utils {

    public static ItemStack getItemFromBackpack(BackPack backPack) {
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta meta = itemStack.getItemMeta();
        
        meta.setDisplayName(backPack.getName());
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
        meta.getPersistentDataContainer().set(backPack.getNamespaceOfBackpackType(), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}