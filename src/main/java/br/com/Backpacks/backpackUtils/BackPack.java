package br.com.Backpacks.backpackUtils;

import br.com.Backpacks.recipes.Recipes;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackPack implements Serializable {

    public Inventory getSecond_page() {
        return second_page;
    }

    public BackpackType getBackpackType() {
        return backpackType;
    }

    public void setBackpackType(BackpackType backpackType) {
        this.backpackType = backpackType;
    }

    private BackpackType backpackType;

    private Inventory second_page;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    private Player owner;

    public Inventory getFirst_page() {
        return first_page;
    }

    public int getBackpack_id() {
        return backpack_id;
    }

    private int backpack_id;

    public void setSecond_page(Inventory second_page) {
        this.second_page = second_page;
    }

    public void setFirst_page(Inventory first_page) {
        this.first_page = first_page;
    }

    private Inventory first_page;

    private Inventory current_page;

    public void setCurrent_page(Inventory current_page) {
        this.current_page = current_page;
    }

    public Inventory getCurrent_page() {
        return current_page;
    }

    public void set_item(int slot, ItemStack item) {
        current_page.setItem(slot, item);
    }

    public int get_First_page_size() {
        return first_page_size;
    }

    public int get_Second_page_size() {
        return second_page_size;
    }

    private int first_page_size;
    private int second_page_size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public BackPack(Player owner, String name, Inventory first_page, int id, BackpackType type) {
        this.backpackType = type;
        this.first_page_size = first_page.getSize();
        this.name = name;
        this.current_page = first_page;
        this.first_page = first_page;
        this.backpack_id = id;
        this.owner = owner;
    }

    public BackPack() {

    }

    public BackPack(Player owner, String name, Inventory first_page, Inventory second_page, int id, BackpackType type) {
        this.backpackType = type;
        this.first_page_size = first_page.getSize();
        this.second_page_size = second_page.getSize();
        this.name = name;
        this.current_page = first_page;
        this.second_page = second_page;
        this.first_page = first_page;
        this.backpack_id = id;
        this.owner = owner;
    }
    public List<String> serialize() {
        List<String> data = new ArrayList<>();

        data.add(String.valueOf(first_page_size));
        data.add(name);
        data.add(backpackType.toString());
        data.add(String.valueOf(backpack_id));
        if(second_page_size > 0) {
            data.add(String.valueOf(second_page_size));
        }

        return data;
    }

    public BackPack deserialize(@NotNull YamlConfiguration config, Player player, String s) {
        List<String> components = (List<String>) config.getList(s + ".i");
        List<ItemStack> list = new ArrayList<>();
        List<ItemStack> list2 = new ArrayList<>();
        if(components.size() > 4){

            second_page_size = Integer.parseInt(components.get(4));
            second_page = Bukkit.createInventory(player, second_page_size, name);
            for(Object item : config.getList(s + ".2")){
                list2.add((ItemStack) item);
            }

            second_page.setStorageContents(list2.toArray(new ItemStack[0]));
        }

        backpackType = BackpackType.valueOf(components.get(0));
        first_page_size = Integer.parseInt(components.get(1));
        name = components.get(2);
        backpack_id = Integer.parseInt(components.get(3));

        first_page = Bukkit.createInventory(player, first_page_size, name);

        for(Object item : config.getList(s + ".1")){
            list.add((ItemStack) item);
        }

        first_page.setStorageContents(list.toArray(new ItemStack[0]));

        current_page = first_page;
        return this;
    }

    public NamespacedKey getNamespaceOfBackpackType() {

        switch (getBackpackType()) {
            case LEATHER -> {
                return new Recipes().getNAMESPACE_LEATHER_BACKPACK();
            }
            case IRON -> {
                return new Recipes().getNAMESPACE_IRON_BACKPACK();
            }
            case GOLD -> {
                return new Recipes().getNAMESPACE_GOLD_BACKPACK();
            }
            case LAPIS -> {
                return new Recipes().getNAMESPACE_LAPIS_BACKPACK();
            }
            case AMETHYST -> {
                return new Recipes().getNAMESPACE_AMETHYST_BACKPACK();
            }
            case DIAMOND -> {
                return new Recipes().getNAMESPACE_DIAMOND_BACKPACK();
            }
            case NETHERITE -> {
                return new Recipes().getNAMESPACE_NETHERITE_BACKPACK();
            }
        }

        return null;
    }

    public void open(Player player){
        player.openInventory(current_page);
    }

}
