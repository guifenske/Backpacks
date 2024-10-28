package br.com.backpacks.utils.menu;

import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Menu {
    protected final Button[] buttons;
    protected final int size;

    public Menu(int size){
        this.buttons = new Button[size];
        this.size = size;
    }

    abstract void addButton(Button button);

    abstract void displayTo(Player player);

    public Button getButton(int slot){
        try{
            return buttons[slot];
        }   catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public int getSize(){
        return this.size;
    }

    public abstract void onClose(Player player, BackPack backPack);

    public abstract void onClickBottomInventory(Player player, BackPack backPack, InventoryClickEvent event);

}
