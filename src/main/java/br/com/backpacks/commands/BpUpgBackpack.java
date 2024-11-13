package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BpUpgBackpack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0){
            sender.sendMessage(Main.getMain().PREFIX + "Please just use: /bpupgbackpack");
            return true;
        }
        if(!(sender instanceof Player player)){
            Bukkit.getConsoleSender().sendMessage(Main.getMain().PREFIX + "This command can only be executed by players!");
            return true;
        }

        if(!player.isOp()){
            player.sendMessage(Main.getMain().PREFIX + "§cYou don't have permission to use this command");
            return true;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(itemStack.getType().isAir()){
            player.sendMessage(Main.getMain().PREFIX + "Item in main hand is null, please use when holding a backpack");
            return true;
        }

        if(!itemStack.hasItemMeta()){
            player.sendMessage(Main.getMain().PREFIX + "Item in main hand isn't a backpack");
            return true;
        }

        BackPack backPack = RecipesUtils.getBackpackFromItem(player.getInventory().getItemInMainHand());

        if(backPack == null){
            player.sendMessage(Main.getMain().PREFIX + "Item in main hand isn't a backpack");
            return true;
        }

        Main.backPackManager.upgradeBackpack(backPack);
        player.sendMessage(Main.getMain().PREFIX + "Backpack upgraded successfully!");

        player.playSound(player, org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        return true;
    }
}
