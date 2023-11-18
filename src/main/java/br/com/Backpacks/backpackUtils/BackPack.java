package br.com.Backpacks.backpackUtils;

import org.bukkit.Bukkit;
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

    private Inventory second_page;

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

    public BackPack(String name, Inventory first_page, int id) {
        this.first_page_size = first_page.getSize();
        this.name = name;
        this.current_page = first_page;
        this.first_page = first_page;
        this.backpack_id = id;
        // Cria a mochila com uma página e seus atributos
    }

    public BackPack() {
        //apenas para serialização
    }

    public BackPack(String name, Inventory first_page, Inventory second_page, int id) {
        this.first_page_size = first_page.getSize();
        this.second_page_size = second_page.getSize();
        this.name = name;
        this.current_page = first_page;
        this.second_page = second_page;
        this.first_page = first_page;
        this.backpack_id = id;
        // Cria a mochila com duas páginas e seus atributos
    }
    public List<String> serialize() {
        List<String> data = new ArrayList<>();

        data.add(String.valueOf(first_page_size));
        data.add(name);
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
        if(components.size() > 3){

            second_page_size = Integer.parseInt(components.get(3));
            second_page = Bukkit.createInventory(player, second_page_size, name);
            for(Object item : config.getList(s + ".2")){
                list2.add((ItemStack) item);
            }

            second_page.setStorageContents(list2.toArray(new ItemStack[0]));
        }

        first_page_size = Integer.parseInt(components.get(0));
        name = components.get(1);
        backpack_id = Integer.parseInt(components.get(2));

        first_page = Bukkit.createInventory(player, first_page_size, name);

        for(Object item : config.getList(s + ".1")){
            list.add((ItemStack) item);
        }

        first_page.setStorageContents(list.toArray(new ItemStack[0]));

        current_page = first_page;
        return this;
    }
}
