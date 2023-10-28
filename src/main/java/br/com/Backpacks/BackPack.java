package br.com.Backpacks;

import org.bukkit.entity.Player;

public class BackPack {
    public BackPack(Player player, int index) {

    }

    private int first_page_size = 0;
    private int second_page_size = 0;

    public int getFirst_page_size() {
        return first_page_size;
    }

    public int getSecond_page_size() {
        return second_page_size;
    }

    public void setFirst_page_size(int first_page_size) {
        this.first_page_size = first_page_size;
    }

    public void setSecond_page_size(int second_page_size) {
        this.second_page_size = second_page_size;
    }

}
