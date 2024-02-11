package br.com.backpacks.utils.inventory;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemCreator {
    private final ItemStack itemStack;

    public ItemCreator(Material material, String name){
        this.itemStack = new ItemStack(material, 1);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);
        this.itemStack.setItemMeta(meta);
    }

    public ItemCreator(Material material, String name, List<String> lore){
        this.itemStack = new ItemStack(material, 1);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);
        this.itemStack.setItemMeta(meta);
    }

    public ItemStack build(){
        return this.itemStack;
    }

}
