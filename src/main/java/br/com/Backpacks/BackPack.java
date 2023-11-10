package br.com.Backpacks;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BackPack {

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

    private String name;

    public BackPack(String name, Inventory first_page, int id) {
        this.first_page_size = first_page.getSize();
        this.name = name;
        this.current_page = first_page;
        this.first_page = first_page;
        this.backpack_id = id;
        // Cria a mochila com uma página e seus atributos
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
}
