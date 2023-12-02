package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    private BackpackType backpackType;

    private Inventory second_page;

    public Inventory getFirst_page() {
        return first_page;
    }

    public int getBackpack_id() {
        return backpack_id;
    }

    private int backpack_id;

    private Inventory first_page;

    public int get_Second_page_size() {
        return second_page_size;
    }

    private int first_page_size;
    private int second_page_size;

    public String getName() {
        return name;
    }
    private String name;

    public BackPack(String name, Inventory first_page, int id, BackpackType type) {
        this.backpackType = type;
        this.first_page_size = first_page.getSize();
        this.name = name;
        this.first_page = first_page;
        this.backpack_id = id;
    }

    public BackPack() {

    }

    public BackPack(String name, Inventory first_page, Inventory second_page, int id, BackpackType type) {
        this.backpackType = type;
        this.first_page_size = first_page.getSize();
        this.second_page_size = second_page.getSize();
        this.name = name;
        this.second_page = second_page;
        this.first_page = first_page;
        this.backpack_id = id;
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

    public BackPack deserialize(YamlConfiguration config, String s) {
        List<String> components = (List<String>) config.getList(s + ".i");

        if(!config.isSet(s + ".i")){
            Bukkit.getConsoleSender().sendMessage(s + ".i" + " not found in the config, please report to the devs");
            return null;
        }

        List<ItemStack> list = new ArrayList<>();
        List<ItemStack> list2 = new ArrayList<>();
        if(components.size() > 4){

            second_page_size = Integer.parseInt(components.get(4));
            second_page = Bukkit.createInventory(null, second_page_size, name);
            for(Object item : config.getList(s + ".2")){
                list2.add((ItemStack) item);
            }

            second_page.setStorageContents(list2.toArray(new ItemStack[0]));
        }

        first_page_size = Integer.parseInt(components.get(0));
        name = components.get(1);
        backpackType = BackpackType.valueOf(components.get(2));
        backpack_id = Integer.parseInt(components.get(3));

        first_page = Bukkit.createInventory(null, first_page_size, name);

        for(Object item : config.getList(s + ".1")){
            list.add((ItemStack) item);
        }

        first_page.setStorageContents(list.toArray(new ItemStack[0]));

        Main.backPackManager.getBackpacks_ids().add(backpack_id);

        return this;
    }

    public NamespacedKey getNamespaceOfBackpackType() {

        switch (getBackpackType()) {
            case LEATHER -> {
                return new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK();
            }
            case IRON -> {
                return new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK();
            }
            case GOLD -> {
                return new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK();
            }
            case LAPIS -> {
                return new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK();
            }
            case AMETHYST -> {
                return new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK();
            }
            case DIAMOND -> {
                return new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK();
            }
            case NETHERITE -> {
                return new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK();
            }
        }

        return null;
    }

    public void open(Player player){
        player.openInventory(first_page);
    }

}
