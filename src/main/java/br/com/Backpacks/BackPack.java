package br.com.Backpacks;

import org.bukkit.entity.Player;

public class BackPack {
    public BackPack(Player player, int backpack_id) {
        set_backpack_id(backpack_id);
    }

    private int first_page_size = 0;
    private int second_page_size = 0;

    private int backpack_id = 0;

    public int get_backpack_id() {
        return backpack_id;
    }

    public int getFirst_page_size() {
        return first_page_size;
    }

    public int getSecond_page_size() {
        return second_page_size;
    }

    public void setFirst_page_size(int first_page_size) {
        this.first_page_size = first_page_size;
    }

    public void set_backpack_id(int backpack_id) {
        this.backpack_id = backpack_id;
    }

    public void setSecond_page_size(int second_page_size) {
        this.second_page_size = second_page_size;
    }

}
