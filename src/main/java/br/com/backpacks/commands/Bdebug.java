package br.com.backpacks.commands;

import br.com.backpacks.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bdebug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player player){
            if(!player.isOp()){
                player.sendMessage(Main.PREFIX + "§cYou don't have permission to use this command");
                return true;
            }
        }

        if(!Main.debugMode){
            Main.debugMode = true;
            Main.getMain().getConfig().set("debug", true);
            return true;
        }

        Main.debugMode = false;
        Main.getMain().getConfig().set("debug", false);
        return true;
    }
}
