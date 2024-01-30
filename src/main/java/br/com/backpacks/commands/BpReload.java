package br.com.backpacks.commands;

import br.com.backpacks.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BpReload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) return false;
        if(sender instanceof Player player){
            if(!player.isOp()){
                player.sendMessage(Main.PREFIX + "§cYou don't have permission to use this command");
                return true;
            }
        }

        Main.getMain().saveDefaultConfig();
        Main.getMain().reloadConfig();
        Main.debugMode = Main.getMain().getConfig().getBoolean("debug");
        if(Main.getMain().getConfig().isSet("autobackup.enabled")) Main.getMain().getBackupHandler().setEnabled(Main.getMain().getConfig().getBoolean("autobackup.enabled"));
        if(Main.getMain().getConfig().isSet("autobackup.keep")) Main.getMain().getBackupHandler().setKeepBackups(Main.getMain().getConfig().getInt("autobackup.keep"));
        if(Main.getMain().getConfig().isSet("autobackup.path")) Main.getMain().getBackupHandler().setPath(Main.getMain().getConfig().getString("autobackup.path"));
        sender.sendMessage(Main.PREFIX + "§aConfig reloaded successfully.");

        return true;
    }
}
