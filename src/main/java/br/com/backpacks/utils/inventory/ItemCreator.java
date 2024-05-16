package br.com.backpacks.utils.inventory;

import br.com.backpacks.recipes.BackpackRecipes;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemCreator {
    private final ItemStack itemStack;
    private Material material;
    private Component name;
    private List<Component> lore;

    public ItemCreator(Material material, String name){
        this.itemStack = new ItemStack(material);
        this.material = material;
        this.name = Component.text(name);
    }

    public ItemCreator(){
        this.itemStack = new ItemStack(Material.AIR);
    }

    public ItemCreator setName(Component component){
        name = component;
        return this;
    }

    public ItemCreator setLore(List<Component> lore) {
        this.lore = lore;
        return this;
    }

    public ItemCreator setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemStack build(){
        itemStack.getItemMeta().displayName(name);
        itemStack.setType(material);
        itemStack.lore(lore);
        itemStack.getItemMeta().getPersistentDataContainer().set(BackpackRecipes.getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);

        return this.itemStack;
    }

}
