package br.com.backpacks.utils.menu;


import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class FixedMenu extends Menu{
    private final String title;

    /**
    * Constructs a FixedMenu with the specified size.
    * A FixedMenu does NOT store the inventory in the cache and does NOT support dynamic changes to it,
    * the menu is constructed at the moment it's displayed to a player.
    * @param size the size of the FixedMenu
    */
    public FixedMenu(int size, String title) {
        super(size);
        this.title = title;
    }

    @Override
    void addButton(Button button) {
        this.buttons[button.getSlot()] = button;
    }

    @Override
    void displayTo(Player player) {
        Inventory inventory = Bukkit.createInventory(null, this.size, title);

        for(Button button : buttons){
            if(button == null) continue;
            inventory.setItem(button.getSlot(), button.getItem());
        }

        player.openInventory(inventory);
    }

    @Override
    public void onClose(Player player, BackPack backPack) {

    }

    @Override
    public void onClickBottomInventory(Player player, BackPack backPack, InventoryClickEvent event) {

    }
}
