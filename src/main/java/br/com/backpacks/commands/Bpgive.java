package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackType;
import br.com.backpacks.events.player.CraftBackpack;
import br.com.backpacks.recipes.RecipesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Bpgive implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 2){
            sender.sendMessage(Main.PREFIX + "§cUsage: /bpgive <player> <backpackType>");
            return true;
        }
        if(sender instanceof Player player){
            if(!player.isOp()){
                player.sendMessage(Main.PREFIX + "§cYou don't have permission to use this command");
                return true;
            }
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            sender.sendMessage(Main.PREFIX + "§cPlayer not found!");
            return true;
        }
        if(!target.isOnline()){
            sender.sendMessage(Main.PREFIX + "§cPlayer is not online!");
            return true;
        }
        BackPack backPack;
        try{
            BackpackType backpackType = BackpackType.valueOf(args[1]);
            backPack = new BackPack(backpackType, CraftBackpack.generateId());
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
        }   catch (IllegalArgumentException e){
            sender.sendMessage(Main.PREFIX + "§cBackpack type not found!");
            return true;
        }

        if(!target.getInventory().addItem(RecipesUtils.getItemFromBackpack(backPack)).isEmpty()){
            sender.sendMessage(Main.PREFIX + "§cPlayer inventory is full! Dropped item on the ground.");
            target.getWorld().dropItem(target.getLocation(), RecipesUtils.getItemFromBackpack(backPack));
            return true;
        }
        sender.sendMessage(Main.PREFIX + "§aBackpack given to " + target.getName());
        target.sendMessage(Main.PREFIX + "§aYou received a backpack from " + sender.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> listBackTypes = List.of("LEATHER", "IRON", "GOLD", "LAPIS", "AMETHYST", "DIAMOND", "NETHERITE");
        List<String> listPlayerNames = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            listPlayerNames.add(player.getName());
        }

        List<String> stringsReturn = new ArrayList<>();

        if(args.length == 1){
            if(args[0].isEmpty()){
                stringsReturn.addAll(listPlayerNames);
            }   else{
                for(String s : listPlayerNames){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[0].equalsIgnoreCase(s.substring(0, i))){
                            stringsReturn.add(s);
                        }
                    }
                }
            }
        }   else if(args.length == 2){
            if(args[1].isEmpty()){
                stringsReturn.addAll(listBackTypes);
            }   else{
                for(String s : listBackTypes){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[1].equalsIgnoreCase(s.substring(0, i))){
                            stringsReturn.add(s);
                        }
                    }
                }
            }
        }

        return stringsReturn;
    }
}
