package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.utils.Constants;
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
        Constants.DEBUG_MODE = Main.getMain().getConfig().getBoolean("debug");
        Constants.CATCH_BACKPACK = Main.getMain().getConfig().getBoolean("fish_backpack");
        Constants.MONSTER_DROPS_BACKPACK = Main.getMain().getConfig().getBoolean("kill_monster_backpack");

        if(Main.getMain().getConfig().isSet("autobackup.keep")) Main.getMain().getBackupHandler().setKeepBackups(Main.getMain().getConfig().getInt("autobackup.keep"));
        sender.sendMessage(Main.PREFIX + "§aConfig reloaded successfully.");

        return true;
    }
}
