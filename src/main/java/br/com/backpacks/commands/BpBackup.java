package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.utils.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BpBackup implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1 || args.length > 3){
            sender.sendMessage("§cUse: /bpbackup <create|remove|rollback>");
            return true;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("create")){
                Main.getMain().getThreadBackpacks().getExecutor().submit(() ->{
                    long time;
                    try {
                        time = Main.getMain().getBackupHandler().backup();
                        if(time != -1L){
                            sender.sendMessage("§aBackup created successfully in " + time + " ms!");
                        }   else{
                            sender.sendMessage("§cSomething went wrong or the backup option is disabled, please check the console for more information.");
                        }
                    }   catch (IOException | InvalidConfigurationException e) {
                        sender.sendMessage("§cAn error occurred while creating the backup, please check the console for more information.");
                        throw new RuntimeException(e);
                    }
                });

                return true;
            }  else if(args[0].equalsIgnoreCase("remove")){
                sender.sendMessage("§cUse: /bpbackup remove <id>");
                return true;
            }   else{
                sender.sendMessage("§cUse: /bpbackup <create|remove|rollback>");
                return true;
            }
        }

        if(args[0].equalsIgnoreCase("rollback")){
            if(args[1].equalsIgnoreCase("undo")){
                Main.backPackManager.setCanBeOpen(false);
                for(UUID uuid : BackpackAction.getHashMap().keySet()){
                    Player player = Bukkit.getPlayer(uuid);
                    BackpackAction.getHashMap().remove(uuid);
                    if(player == null) continue;
                    player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                }

                Main.getMain().getThreadBackpacks().getExecutor().submit(() -> {
                    try{
                        long time = Main.getMain().getBackupHandler().undoRollback();
                        if(time != -1L){
                            sender.sendMessage("§aBackup restored successfully in " + time + " ms!");
                        }   else{
                            sender.sendMessage("§cYou never did a rollback to undo it.");
                        }
                        return true;
                    }   catch (IOException e){
                        Main.backPackManager.setCanBeOpen(true);
                        sender.sendMessage("§cAn error occurred while restoring the backup, please check the console for more information.");
                        throw new RuntimeException(e);
                    }
                });
                return true;
            }

            if(!Main.getMain().getBackupHandler().getBackupsNames().contains(args[1])){
                sender.sendMessage("§cBackup not found.");
                return true;
            }

            Main.backPackManager.setCanBeOpen(false);
            for(UUID uuid : BackpackAction.getHashMap().keySet()){
                Player player = Bukkit.getPlayer(uuid);
                BackpackAction.getHashMap().remove(uuid);
                if(player == null) continue;
                player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
            }

            Main.getMain().getThreadBackpacks().getExecutor().submit(() -> {
                try{
                    long time = Main.getMain().getBackupHandler().restoreBackup(args[1]);
                    if(time != -1L){
                        sender.sendMessage("§aBackup restored successfully in " + time + " ms!");
                    }   else{
                        sender.sendMessage("§cSomething went wrong, please check the console for more information.");
                    }
                    return true;
                }   catch (IOException e){
                    Main.backPackManager.setCanBeOpen(true);
                    sender.sendMessage("§cAn error occurred while restoring the backup, please check the console for more information.");
                    throw new RuntimeException(e);
                }
            });
            return true;
        }


        if (args[0].equalsIgnoreCase("remove")) {
            if(!Main.getMain().getBackupHandler().getBackupsNames().contains(args[1])){
                sender.sendMessage("§cBackup not found.");
                return true;
            }

            Main.getMain().getThreadBackpacks().getExecutor().submit(() -> {
                if (Main.getMain().getBackupHandler().removeBackup(args[1])) {
                    sender.sendMessage("§aBackup removed successfully.");
                } else {
                    sender.sendMessage("§cSomething went wrong, please check the console for more information.");
                }
                return true;
            });
        }   else {
            sender.sendMessage("§cUse: /bpbackup <create|remove|restore>");
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> stringList = new ArrayList<>();
        List<String> backups = Main.getMain().getBackupHandler().getBackupsNames();
        backups.add("undo");
        if(args.length == 1){
            if(args[0].isEmpty()){
                stringList.add("create");
                stringList.add("remove");
                stringList.add("rollback");
            }   else{
                for(String s : List.of("create", "remove", "rollback")){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[0].equalsIgnoreCase(s.substring(0, i))){
                            stringList.add(s);
                        }
                    }
                }
            }
        }   else if(args.length == 2){
            if(args[1].isEmpty() && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rollback"))){
                stringList.addAll(backups);
            }   else{
                for(String s : backups){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[1].equalsIgnoreCase(s.substring(0, i))){
                            stringList.add(s);
                        }
                    }
                }
            }
        }
        return stringList;
    }
}
