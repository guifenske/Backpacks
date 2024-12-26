package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackType;
import br.com.backpacks.backpack.RandomBackpackBuilder;
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

public class BpGive implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 2){
            sender.sendMessage(Main.getMain().PREFIX + "§cUsage: /bpgive <player> <backpackType>");
            return true;
        }

        if(sender instanceof Player player){
            if(!player.isOp()){
                player.sendMessage(Main.getMain().PREFIX + "§cYou don't have permission to use this command");
                return true;
            }
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null){
            sender.sendMessage(Main.getMain().PREFIX + "§cPlayer not found!");
            return true;
        }

        if(!target.isOnline()){
            sender.sendMessage(Main.getMain().PREFIX + "§cPlayer is not online!");
            return true;
        }

        Backpack backpack;

        if(!args[1].equalsIgnoreCase("RANDOM")){
            try{
                BackpackType backpackType = BackpackType.valueOf(args[1]);
                backpack = new Backpack(backpackType, Main.backpackManager.getLastBackpackID() + 1);
            }   catch (IllegalArgumentException e){
                sender.sendMessage(Main.getMain().PREFIX + "§cBackpack type not found!");
                return true;
            }
        }   else{
            backpack = new RandomBackpackBuilder("Random Backpack", Main.backpackManager.getLastBackpackID() + 1).generateBackpack();
        }

        Main.backpackManager.getBackpacks().put(backpack.getId(), backpack);
        Main.backpackManager.setLastBackpackID(Main.backpackManager.getLastBackpackID() + 1);

        if(!target.getInventory().addItem(RecipesUtils.getItemFromBackpack(backpack)).isEmpty()){
            sender.sendMessage(Main.getMain().PREFIX + "§cPlayer inventory is full! Dropped item on the ground.");
            target.getWorld().dropItem(target.getLocation(), RecipesUtils.getItemFromBackpack(backpack));
            return true;
        }

        sender.sendMessage(Main.getMain().PREFIX + "§aBackpack given to " + target.getName());
        target.sendMessage(Main.getMain().PREFIX + "§aYou received a backpack from " + sender.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> listBackTypes = List.of("LEATHER", "IRON", "GOLD", "LAPIS", "AMETHYST", "DIAMOND", "NETHERITE", "RANDOM");
        List<String> listPlayerNames = new ArrayList<>();

        for(Player player : Bukkit.getOnlinePlayers()){
            listPlayerNames.add(player.getName());
        }

        List<String> stringsReturn = new ArrayList<>();

        if(args.length == 1){

            if(args[0].isEmpty()){
                stringsReturn.addAll(listPlayerNames);
            }

            else{
                for(String s : listPlayerNames){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[0].equalsIgnoreCase(s.substring(0, i))){
                            stringsReturn.add(s);
                        }
                    }
                }
            }
        }

        else if(args.length == 2){

            if(args[1].isEmpty()){
                stringsReturn.addAll(listBackTypes);
            }

            else{
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
