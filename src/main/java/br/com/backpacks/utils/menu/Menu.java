package br.com.backpacks.utils.menu;

import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class Menu {
    private final Button[] buttons;
    private final int size;
    protected final BackPack backPack;
    protected final Inventory inventory;

    public Menu(int size, String title, BackPack backPack){
        this.buttons = new Button[size];
        this.size = size;
        this.backPack = backPack;
        this.inventory = Bukkit.createInventory(null, size, backPack.getName() + "'s " + title);
    }

    public int getSize(){
        return this.size;
    }

    protected void addButton(Button button){
        buttons[button.getSlot()] = button;
        refreshButton(button);
    }

    protected void refreshButton(Button button){
        inventory.setItem(button.getSlot(), button.getItem());
    }

    public void displayTo(Player player){
        player.openInventory(inventory);
    }

    public Button getButton(int slot){
        try{
            return buttons[slot];
        }   catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public abstract void onClose(Player player);

    public abstract void onClickBottomInventory(Player player, InventoryClickEvent event);

}
