package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import br.com.backpacks.menu.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BpList implements CommandExecutor, Listener {
    private HashMap<UUID, Integer> page = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender){
            sender.sendMessage(Main.getMain().PREFIX + "§cThis command can only be used by players.");
            return true;
        }

        if(args.length > 0){
            sender.sendMessage(Main.getMain().PREFIX + "§cInvalid arguments, use just /bplist");
            return true;
        }

        Player player = (Player) sender;

        if(!player.isOp()){
            player.sendMessage(Main.getMain().PREFIX + "§cYou don't have permission to use this command.");
            return true;
        }

        page.put(player.getUniqueId(), 0);
        player.openInventory(inventory(player));
        Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> BackpackAction.getSpectators().put(player.getUniqueId(), true), 1L);

        return true;
    }

    private Inventory inventory(Player player){
        Inventory inventory = Bukkit.createInventory(player, 54, "§8Backpacks List");
        List<Integer> backpacksIds = new ArrayList<>(Main.backpackManager.getBackpacks().keySet());
        ItemStack nextPage = new ItemCreator(Material.ARROW, "§aNext Page").build();
        ItemStack previousPage = new ItemCreator(Material.ARROW, "§aPrevious Page").build();
        inventory.setItem(52, previousPage);
        inventory.setItem(53, nextPage);

        for(int i = 0; i < backpacksIds.size(); i++){
            if(i == 52 || backpacksIds.size() < page.get(player.getUniqueId()) * 52 + i) break;
            Backpack backpack = Main.backpackManager.getBackpackFromId(backpacksIds.get(page.get(player.getUniqueId()) * 52 + i));
            ItemStack backpackItem = RecipesUtils.getItemFromBackpack(backpack);

            ItemMeta meta = backpackItem.getItemMeta();
            meta.setLore(List.of("Id: " + backpack.getId()));

            if(backpack.isBlock()){
                meta.setLore(List.of("Id: " + backpack.getId(), "Location: " + backpack.getLocation().getX() + " " + backpack.getLocation().getY() + " " + backpack.getLocation().getZ()));
            }

            backpackItem.setItemMeta(meta);
            inventory.setItem(i, backpackItem);
        }

        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getSpectators().containsKey(event.getWhoClicked().getUniqueId())) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        //for some stupid reason the Click event is being called before the page is set??????
        if(!page.containsKey(player.getUniqueId())){
            page.put(player.getUniqueId(), 0);
        }

        if(event.getRawSlot() == 52){
            if(page.get(player.getUniqueId()) < 1){
                player.sendMessage(Main.getMain().PREFIX + "§cYou are already in the first page.");
                return;
            }

            page.put(player.getUniqueId(), page.get(player.getUniqueId()) - 1);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                player.openInventory(inventory(player));
                BackpackAction.getSpectators().put(player.getUniqueId(), true);
            }, 1L);

            return;
        }

        if(event.getRawSlot() == 53){
            if(page.get(player.getUniqueId()) >= Main.backpackManager.getBackpacks().size() / 52){
                player.sendMessage(Main.getMain().PREFIX + "§cYou are already in the last page.");
                return;
            }

            page.put(player.getUniqueId(), page.get(player.getUniqueId()) + 1);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                player.openInventory(inventory(player));
                BackpackAction.getSpectators().put(player.getUniqueId(), true);
            }, 1L);

            return;
        }

        if(event.getRawSlot() > 52) return;
        if(event.getCurrentItem() == null) return;

        Backpack backpack = Main.backpackManager.getBackpackFromId(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER));

        BackpackAction.clearPlayerAction(player);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
            backpack.open(player);
        }, 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getSpectators().containsKey(event.getPlayer().getUniqueId())) return;
        BackpackAction.getSpectators().remove(event.getPlayer().getUniqueId());
        BackpackAction.clearPlayerAction(event.getPlayer());
        page.remove(event.getPlayer().getUniqueId());
    }
}
