package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.RecipesUtils;
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
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BpList implements CommandExecutor, Listener {
    private final HashMap<UUID, Integer> page = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender){
            sender.sendMessage(Main.PREFIX + "§cThis command can only be used by players.");
            return false;
        }
        if(args.length > 0){
            sender.sendMessage(Main.PREFIX + "§cInvalid arguments, use just /bplist");
            return false;
        }
        Player player = (Player) sender;
        if(!player.isOp()){
            player.sendMessage(Main.PREFIX + "§cYou don't have permission to use this command.");
            return false;
        }
        page.put(player.getUniqueId(), 0);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), ()-> {
            player.openInventory(inventory(player));
            BackpackAction.setAction(player, BackpackAction.Action.BPLIST);
        }, 1L);

        return true;
    }

    private Inventory inventory(Player player){
        Inventory inventory = Bukkit.createInventory(player, 54, "§8Backpacks List");
        List<Integer> backpacksIds = new ArrayList<>(Main.backPackManager.getBackpacks().keySet());
        ItemStack nextPage = new ItemCreator(Material.ARROW, "§aNext Page").build();
        ItemStack previousPage = new ItemCreator(Material.ARROW, "§aPrevious Page").build();
        inventory.setItem(52, previousPage);
        inventory.setItem(53, nextPage);

        for(int i = 0; i < backpacksIds.size(); i++){
            if(i == 52 || backpacksIds.size() < page.get(player.getUniqueId()) * 52 + i) break;
            inventory.setItem(i, RecipesUtils.getItemFromBackpack(Main.backPackManager.getBackpackFromId(backpacksIds.get(page.get(player.getUniqueId()) * 52 + i))));
        }

        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event){
        if(!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.BPLIST)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        //for some stupid reason the Click event is being called before the page is set??????
        if(!page.containsKey(player.getUniqueId())){
            page.put(player.getUniqueId(), 0);
        }

        if(event.getRawSlot() == 52){
            if(page.get(player.getUniqueId()) < 1){
                player.sendMessage(Main.PREFIX + "§cYou are already in the first page.");
                return;
            }
            page.put(player.getUniqueId(), page.get(player.getUniqueId()) - 1);
            BackpackAction.removeAction(player);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                player.openInventory(inventory(player));
                BackpackAction.setAction(player, BackpackAction.Action.BPLIST);
            }, 1L);
            return;
        }
        if(event.getRawSlot() == 53){
            if(page.get(player.getUniqueId()) >= Main.backPackManager.getBackpacks().size() / 52){
                player.sendMessage(Main.PREFIX + "§cYou are already in the last page.");
                return;
            }
            page.put(player.getUniqueId(), page.get(player.getUniqueId()) + 1);
            BackpackAction.removeAction(player);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
                player.openInventory(inventory(player));
                BackpackAction.setAction(player, BackpackAction.Action.BPLIST);
            }, 1L);
            return;
        }
        if(event.getRawSlot() > 52) return;
        if(event.getCurrentItem() == null) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));

        BackpackAction.removeAction(player);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), ()->{
            backPack.open(player);
        }, 1L);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.BPLIST)) return;
        BackpackAction.removeAction((Player) event.getPlayer());
        page.remove(event.getPlayer().getUniqueId());
    }
}