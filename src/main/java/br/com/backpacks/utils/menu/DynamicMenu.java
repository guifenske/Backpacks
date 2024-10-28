package br.com.backpacks.utils.menu;

import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class DynamicMenu extends Menu {
    protected final Inventory inventory;

    public DynamicMenu(int size, String title){
        super(size);
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    protected void refreshButtonInMenu(Button button){
        this.inventory.setItem(button.getSlot(), button.getItem());
    }

    @Override
    protected final void addButton(Button button){
        this.buttons[button.getSlot()] = button;
        refreshButtonInMenu(button);
    }

    @Override
    public void displayTo(Player player){
        player.openInventory(this.inventory);
    }

    @Override
    public void onClose(Player player, BackPack backPack) {
    }

    @Override
    public void onClickBottomInventory(Player player, BackPack backPack, InventoryClickEvent event){
        event.setCancelled(true);
    }

}
