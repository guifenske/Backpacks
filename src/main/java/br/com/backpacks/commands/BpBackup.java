package br.com.backpacks.commands;

import br.com.backpacks.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BpBackup implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1 || args.length > 2){
            sender.sendMessage("§cUse: /bpbackup <create|remove>");
            return false;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("create")){
                try {
                    Main.getMain().getBackupHandler().backup(Main.getMain().getBackupHandler().getPath());
                } catch (IOException | InvalidConfigurationException e) {
                    sender.sendMessage("§cAn error occurred while creating the backup, please check the console for more information.");
                    throw new RuntimeException(e);
                }
                sender.sendMessage("§aBackup created successfully.");
                return true;
            }  else if(args[0].equalsIgnoreCase("remove")){
                sender.sendMessage("§cUse: /bpbackup remove <id>");
                return false;
            }   else{
                sender.sendMessage("§cUse: /bpbackup <create|remove>");
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if(!Main.getMain().getBackupHandler().getBackups().contains(args[1])){
                sender.sendMessage("§cBackup not found.");
                return false;
            }

            if (Main.getMain().getBackupHandler().removeBackup(args[1])) {
                sender.sendMessage("§aBackup removed successfully.");
                return true;
            } else {
                sender.sendMessage("§cSomething went wrong, please check the console for more information.");
                return false;
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            sender.sendMessage("§cUse just: /bpbackup create");
            return false;
        } else {
            sender.sendMessage("§cUse: /bpbackup create, or /bpbackup remove <fileName>");
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> stringList = new ArrayList<>();
        List<String> backups = Main.getMain().getBackupHandler().getBackups();
        if(args.length == 1){
            if(args[0].isEmpty()){
                stringList.add("create");
                stringList.add("remove");
            }   else{
                for(String s : List.of("create", "remove")){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[0].equalsIgnoreCase(s.substring(0, i))){
                            stringList.add(s);
                        }
                    }
                }
            }
        }   else if(args.length == 2){
            if(args[1].isEmpty() && args[0].equalsIgnoreCase("remove")){
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
